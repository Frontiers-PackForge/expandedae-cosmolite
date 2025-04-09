package lu.kolja.expandedae.menu;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import lu.kolja.expandedae.block.entity.FilterContainerGroup;
import lu.kolja.expandedae.item.part.FilterTerminalPart;
import lu.kolja.expandedae.helper.FilterContainer;
import lu.kolja.expandedae.packets.ClearFilterTerminalPacket;
import lu.kolja.expandedae.packets.FilterTerminalPacket;
import org.jetbrains.annotations.Nullable;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.util.IConfigurableObject;
import appeng.blockentity.misc.InterfaceBlockEntity;
import appeng.core.AELog;
import appeng.helpers.InventoryAction;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.implementations.UpgradeableMenu;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.FilteredInternalInventory;
import appeng.util.inv.filter.IAEItemFilter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class FilterTermMenu extends UpgradeableMenu<FilterTerminalPart> {
    private final IConfigurableObject host;

    public static final MenuType<FilterTermMenu> TYPE = MenuTypeBuilder.create(FilterTermMenu::new, FilterTerminalPart.class).build("filter_terminal"); //TODO
    private static long inventorySerial = Long.MIN_VALUE;
    private final Map<FilterContainer, ContainerTracker> diList;
    private final Long2ObjectOpenHashMap<ContainerTracker> byId;
    private final Set<FilterContainer> pinnedHosts;

    public FilterTermMenu(MenuType<? extends FilterTermMenu> menuType, int id, Inventory ip, FilterTerminalPart host) {
        super(menuType, id, ip, host);
        this.diList = new IdentityHashMap<>();
        this.byId = new Long2ObjectOpenHashMap<>();
        this.pinnedHosts = Collections.newSetFromMap(new IdentityHashMap<>());
        this.host = host;
    }

    public void broadcastChanges() {
        if (!this.isClientSide()) {
            //super.broadcastChanges();
            IGrid grid = this.getGrid();
            VisitorState state = new VisitorState();
            if (grid != null) {
                for(Class<?> machineClass : grid.getMachineClasses()) {
                    if (FilterContainer.class.isAssignableFrom(machineClass)) {
                        Class<? extends FilterContainer> filterClass = (Class<? extends FilterContainer>) machineClass;
                        this.visitFilterHosts(grid, filterClass, state);
                    }
                }
                this.pinnedHosts.removeIf((host) -> host.getGrid() != grid);
            } else {
                this.pinnedHosts.clear();
            }
            if (state.total == this.diList.size() && !state.forceFullUpdate) {
                this.sendIncrementalUpdate();
            } else {
                this.sendFullUpdate(grid);
            }

        }
    }

    private @Nullable IGrid getGrid() {
        IActionHost host = this.getActionHost();
        if (host != null) {
            IGridNode agn = host.getActionableNode();
            if (agn != null && agn.isActive()) {
                return agn.getGrid();
            }
        }
        return null;
    }

    private boolean isFull(FilterContainer logic) {
        for(int i = 0; i < logic.getFilteredInventory().size(); ++i) {
            if (logic.getFilteredInventory().getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void visitFilterHosts(IGrid grid, Class<? extends FilterContainer> machineClass, VisitorState state) {
        for(InterfaceBlockEntity container : grid.getActiveMachines(InterfaceBlockEntity.class)) {
            ContainerTracker tracker = this.diList.get(container);
            if (tracker == null || !tracker.group.equals(container.getInterfaceLogic())) {
                state.forceFullUpdate = true;
            }
        }
    }

    public void doAction(ServerPlayer player, InventoryAction action, int slot, long id) {
        ContainerTracker inv = this.byId.get(id);
        if (inv != null) {
            if (slot >= 0 && slot < inv.server.size()) {
                ItemStack is = inv.server.getStackInSlot(slot);
                FilteredInternalInventory fakeSlot = new FilteredInternalInventory(inv.server.getSlotInv(slot), new FakeSlotFilter());
                ItemStack carried = this.getCarried();
                switch (action) {
                    case PICKUP_OR_SET_DOWN:
                        if (!carried.isEmpty()) {
                            ItemStack inSlot = fakeSlot.getStackInSlot(0);
                            if (inSlot.isEmpty()) {
                                this.setCarried(fakeSlot.addItems(carried));
                            } else {
                                inSlot = inSlot.copy();
                                ItemStack inHand = carried.copy();
                                fakeSlot.setItemDirect(0, ItemStack.EMPTY);
                                this.setCarried(ItemStack.EMPTY);
                                this.setCarried(fakeSlot.addItems(inHand.copy()));
                                if (this.getCarried().isEmpty()) {
                                    this.setCarried(inSlot);
                                } else {
                                    this.setCarried(inHand);
                                    fakeSlot.setItemDirect(0, inSlot);
                                }
                            }
                        } else {
                            this.setCarried(fakeSlot.getStackInSlot(0));
                            fakeSlot.setItemDirect(0, ItemStack.EMPTY);
                        }
                        break;
                    case SPLIT_OR_PLACE_SINGLE:
                        if (!carried.isEmpty()) {
                            ItemStack extra = carried.split(1);
                            if (!extra.isEmpty()) {
                                extra = fakeSlot.addItems(extra);
                            }

                            if (!extra.isEmpty()) {
                                carried.grow(extra.getCount());
                            }
                        } else if (!is.isEmpty()) {
                            this.setCarried(fakeSlot.extractItem(0, (is.getCount() + 1) / 2, false));
                        }
                        break;
                    case SHIFT_CLICK:
                        ItemStack stack = fakeSlot.getStackInSlot(0).copy();
                        if (!player.getInventory().add(stack)) {
                            fakeSlot.setItemDirect(0, stack);
                        } else {
                            fakeSlot.setItemDirect(0, ItemStack.EMPTY);
                        }
                        break;
                    case MOVE_REGION:
                        for(int x = 0; x < inv.server.size(); ++x) {
                            ItemStack slotStack = inv.server.getStackInSlot(x);
                            if (!player.getInventory().add(slotStack)) {
                                fakeSlot.setItemDirect(0, slotStack);
                            } else {
                                fakeSlot.setItemDirect(0, ItemStack.EMPTY);
                            }
                        }
                        break;
                    case CREATIVE_DUPLICATE:
                        if (player.getAbilities().instabuild && carried.isEmpty()) {
                            this.setCarried(is.isEmpty() ? ItemStack.EMPTY : is.copy());
                        }
                }

            } else {
                AELog.warn("Client refers to invalid slot %d of inventory %s", slot, inv.container);
            }
        }
    }

    private void sendFullUpdate(@Nullable IGrid grid) {
        this.byId.clear();
        this.diList.clear();
        this.sendPacketToClient(new ClearFilterTerminalPacket());
        if (grid != null) {
            for(Class<?> machineClass : grid.getMachineClasses()) {
                Class<? extends FilterContainer> containerClass = tryCastMachineToContainer(machineClass);
                if (containerClass != null) {
                    for(FilterContainer container : grid.getActiveMachines(containerClass)) {
                        this.diList.put(container, new ContainerTracker(container, container.getFilteredInventory(), container.getTerminalGroup()));
                    }
                }
            }

            for(ContainerTracker inv : this.diList.values()) {
                this.byId.put(inv.serverId, inv);
                this.sendPacketToClient(inv.createFullPacket());
            }

        }
    }

    private void sendIncrementalUpdate() {
        for(ContainerTracker inv : this.diList.values()) {
            FilterTerminalPacket packet = inv.createUpdatePacket();
            if (packet != null) {
                this.sendPacketToClient(packet);
            }
        }

    }

    private static Class<? extends FilterContainer> tryCastMachineToContainer(Class<?> machineClass) {
        return FilterContainer.class.isAssignableFrom(machineClass) ? machineClass.asSubclass(FilterContainer.class) : null;
    }

    private static class VisitorState {
        int total;
        boolean forceFullUpdate;

        private VisitorState() {
        }
    }
    private static class ContainerTracker {
        private final FilterContainer container;
        private final long sortBy;
        private final long serverId;
        private final FilterContainerGroup group;
        private final InternalInventory client;
        private final InternalInventory server;

        public ContainerTracker(FilterContainer container, InternalInventory filters, FilterContainerGroup group) {
            this.serverId = FilterTermMenu.inventorySerial++;
            this.container = container;
            this.server = filters;
            this.client = new AppEngInternalInventory(this.server.size());
            this.group = group;
            this.sortBy = container.getTerminalSortOrder();
        }

        public FilterTerminalPacket createFullPacket() {
            Int2ObjectArrayMap<ItemStack> slots = new Int2ObjectArrayMap<>(this.server.size());

            for (int i = 0; i < this.server.size(); i++) {
                ItemStack stack = this.server.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    slots.put(i, stack);
                }
            }
            return FilterTerminalPacket.fullUpdate(this.serverId, this.server.size(), this.sortBy, this.group, slots);
        }

        public @Nullable FilterTerminalPacket createUpdatePacket() {
            IntList changedSlots = this.detectChangedSlots();
            if (changedSlots == null) {
                return null;
            } else {
                Int2ObjectArrayMap<ItemStack> slots = new Int2ObjectArrayMap<>(changedSlots.size());

                for (int i = 0; i < changedSlots.size(); i++) {
                    int slot = changedSlots.getInt(i);
                    ItemStack stack = this.server.getStackInSlot(slot);
                    this.client.setItemDirect(slot, stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
                    slots.put(slot, stack);
                }
                return FilterTerminalPacket.incrementalUpdate(this.serverId, slots);
            }
        }
        private @Nullable IntList detectChangedSlots() {
            IntList changedSlots = null;

            for(int x = 0; x < this.server.size(); ++x) {
                if (isDifferent(this.server.getStackInSlot(x), this.client.getStackInSlot(x))) {
                    if (changedSlots == null) {
                        changedSlots = new IntArrayList();
                    }

                    changedSlots.add(x);
                }
            }

            return changedSlots;
        }
        private static boolean isDifferent(ItemStack a, ItemStack b) {
            if (a.isEmpty() && b.isEmpty()) {
                return false;
            } else if (!a.isEmpty() && !b.isEmpty()) {
                return !ItemStack.matches(a, b);
            } else {
                return true;
            }
        }
    }

    private static class FakeSlotFilter implements IAEItemFilter {
        private FakeSlotFilter() {
        }

        public boolean allowExtract(InternalInventory inv, int slot, ItemStack stack) {
            return true;
        }

        public boolean allowInsert(InternalInventory inv, int slot, ItemStack stack) {
            return !stack.isEmpty();
        }
    }
}
