package lu.kolja.expandedae.screen;

import java.util.*;

import lu.kolja.expandedae.block.entity.FilterContainerGroup;
import lu.kolja.expandedae.client.gui.FilterContainerRecord;
import lu.kolja.expandedae.menu.FilterTermMenu;
import com.google.common.collect.HashMultimap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import appeng.api.config.Settings;
import appeng.api.config.TerminalStyle;
import appeng.api.stacks.AEItemKey;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.style.PaletteColor;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.AETextField;
import appeng.client.gui.widgets.Scrollbar;
import appeng.client.gui.widgets.SettingToggleButton;
import appeng.client.guidebook.document.LytRect;
import appeng.client.guidebook.render.SimpleRenderContext;
import appeng.core.AEConfig;
import appeng.core.AppEng;
import appeng.core.localization.GuiText;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.InventoryActionPacket;
import appeng.helpers.InventoryAction;
import appeng.menu.slot.FakeSlot;
import appeng.util.inv.AppEngInternalInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public class FilterTermScreen<C extends FilterTermMenu> extends AEBaseScreen<C> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterTermScreen.class);
    private static final int GUI_WIDTH = 195;
    private static final int GUI_TOP_AND_BOTTOM_PADDING = 54;
    private static final int GUI_PADDING_X = 8;
    private static final int GUI_PADDING_Y = 6;
    private static final int GUI_HEADER_HEIGHT = 17;
    private static final int GUI_FOOTER_HEIGHT = 97;
    private static final int COLUMNS = 9;
    private static final int PATTERN_PROVIDER_NAME_MARGIN_X = 2;
    private static final int TEXT_MAX_WIDTH = 155;
    private static final int ROW_HEIGHT = 18;
    private static final int SLOT_SIZE = 18;
    private static final Rect2i HEADER_BBOX = new Rect2i(0, 0, 195, 17);
    private static final Rect2i ROW_TEXT_TOP_BBOX = new Rect2i(0, 17, 195, 18);
    private static final Rect2i ROW_TEXT_MIDDLE_BBOX = new Rect2i(0, 53, 195, 18);
    private static final Rect2i ROW_TEXT_BOTTOM_BBOX = new Rect2i(0, 89, 195, 18);
    private static final Rect2i ROW_INVENTORY_TOP_BBOX = new Rect2i(0, 35, 195, 18);
    private static final Rect2i ROW_INVENTORY_MIDDLE_BBOX = new Rect2i(0, 71, 195, 18);
    private static final Rect2i ROW_INVENTORY_BOTTOM_BBOX = new Rect2i(0, 107, 195, 18);
    private static final Rect2i FOOTER_BBOX = new Rect2i(0, 125, 195, 97);
    private static final Comparator<FilterContainerGroup> GROUP_COMPARATOR = Comparator.comparing((group) -> group.name().getString().toLowerCase(Locale.ROOT));
    private final HashMap<Long, FilterContainerRecord> byId = new HashMap<>();
    private final HashMultimap<FilterContainerGroup, FilterContainerRecord> byGroup = HashMultimap.create();
    private final ArrayList<FilterContainerGroup> groups = new ArrayList<>();
    private final ArrayList<FilterTermScreen.Row> rows = new ArrayList<>();
    private final Map<String, Set<Object>> cachedSearches = new WeakHashMap<>();
    private final Scrollbar scrollbar;
    private final AETextField searchField;
    private int visibleRows = 0;

    public FilterTermScreen(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.scrollbar = this.widgets.addScrollBar("scrollbar");
        this.imageWidth = 195;
        TerminalStyle terminalStyle = AEConfig.instance().getTerminalStyle();
        this.addToLeftToolbar(new SettingToggleButton(Settings.TERMINAL_STYLE, terminalStyle, this::toggleTerminalStyle));
        this.searchField = this.widgets.addTextField("search");
        this.searchField.setResponder((str) -> this.refreshList());
        this.searchField.setPlaceholder(GuiText.SearchPlaceholder.text());
    }

    public void init() {
        this.visibleRows = this.config.getTerminalStyle().getRows((this.height - 17 - 97 - 54) / 18);
        this.imageHeight = 114 + this.visibleRows * 18;
        super.init();
        this.setInitialFocus(this.searchField);
        this.resetScrollbar();
    }

    public void drawFG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY) {
        this.menu.slots.removeIf((slot) -> slot instanceof FakeSlot);
        int textColor = this.style.getColor(PaletteColor.DEFAULT_TEXT_COLOR).toARGB();
        ClientLevel level = Minecraft.getInstance().level;
        int scrollLevel = this.scrollbar.getCurrentScroll();

        for(int i = 0; i < this.visibleRows; ++i) {
            if (scrollLevel + i < this.rows.size()) {
                FilterTermScreen.Row row = this.rows.get(scrollLevel + i);
                if (row instanceof FilterTermScreen.SlotsRow slotsRow) {
                    FilterContainerRecord container = slotsRow.container;
                    AppEngInternalInventory inventory = container.getInventory();

                    for(int col = 0; col < slotsRow.slots; ++col) {
                        FakeSlot slot = new FakeSlot(inventory, slotsRow.offset + col);
                        this.menu.slots.add(slot);
                        ItemStack item = container.getInventory().getStackInSlot(slotsRow.offset + col);
                        if (!item.isEmpty()) {
                            guiGraphics.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, 2147418112);
                        }
                    }
                } else if (row instanceof FilterTermScreen.GroupHeaderRow headerRow) {
                    FilterContainerGroup group = headerRow.group;
                    if (group.icon() != null) {
                        SimpleRenderContext renderContext = new SimpleRenderContext(LytRect.empty(), guiGraphics);
                        renderContext.renderItem(group.icon().getReadOnlyStack(), 10, 23 + i * 18, 8.0F, 8.0F);
                    }

                    int rows = this.byGroup.get(group).size();
                    FormattedText displayName;
                    if (rows > 1) {
                        displayName = Component.empty().append(group.name()).append(Component.literal(" (" + rows + ")"));
                    } else {
                        displayName = group.name();
                    }

                    FormattedCharSequence text = Language.getInstance().getVisualOrder(this.font.substrByWidth(displayName, 145));
                    guiGraphics.drawString(this.font, text, 20, 23 + i * 18, textColor, false);
                }
            }
        }

    }

    protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
        if (this.hoveredSlot == null) {
            int hoveredLineIndex = this.getHoveredLineIndex(x, y);
            if (hoveredLineIndex != -1) {
                FilterTermScreen.Row row = this.rows.get(hoveredLineIndex);
                if (row instanceof FilterTermScreen.GroupHeaderRow headerRow) {
                    if (!headerRow.group.tooltip().isEmpty()) {
                        guiGraphics.renderTooltip(this.font, headerRow.group.tooltip(), Optional.empty(), x, y);
                        return;
                    }
                }
            }
        }
        super.renderTooltip(guiGraphics, x, y);
    }

    private int getHoveredLineIndex(int x, int y) {
        x = x - this.leftPos - 8;
        y = y - this.topPos - 18;
        if (x >= 0 && y >= 0) {
            if (x < 162 && y < this.visibleRows * 18) {
                int rowIndex = this.scrollbar.getCurrentScroll() + y / 18;
                return rowIndex >= 0 && rowIndex < this.rows.size() ? rowIndex : -1;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public boolean mouseClicked(double xCoord, double yCoord, int btn) {
        if (btn == 1 && this.searchField.isMouseOver(xCoord, yCoord)) {
            this.searchField.setValue("");
        }

        return super.mouseClicked(xCoord, yCoord, btn);
    }

    protected void slotClicked(Slot slot, int slotIdx, int mouseButton, ClickType clickType) {
        if (slot instanceof FakeSlot) {
            InventoryAction action = null;
            switch (clickType) {
                case PICKUP:
                    action = mouseButton == 1 ? InventoryAction.SPLIT_OR_PLACE_SINGLE : InventoryAction.PICKUP_OR_SET_DOWN;
                    break;
                case QUICK_MOVE:
                    action = mouseButton == 1 ? InventoryAction.PICKUP_SINGLE : InventoryAction.SHIFT_CLICK;
                    break;
                case CLONE:
                    if (this.getPlayer().getAbilities().instabuild) {
                        action = InventoryAction.CREATIVE_DUPLICATE;
                    }
                case THROW:
            }

            if (action != null) {
                FakeSlot machineSlot = (FakeSlot) slot;
                InventoryActionPacket p = new InventoryActionPacket(action, machineSlot.x, machineSlot.getItem()); //TODO maybe broken?!??
                NetworkHandler.instance().sendToServer(p);
            }

        } else {
            super.slotClicked(slot, slotIdx, mouseButton, clickType);
        }
    }

    public void drawBG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY, float partialTicks) {
        this.blit(guiGraphics, offsetX, offsetY, HEADER_BBOX);
        int scrollLevel = this.scrollbar.getCurrentScroll();
        int currentY = offsetY + 17;
        this.blit(guiGraphics, offsetX, currentY + this.visibleRows * 18, FOOTER_BBOX);

        for(int i = 0; i < this.visibleRows; ++i) {
            boolean firstLine = i == 0;
            boolean lastLine = i == this.visibleRows - 1;
            Rect2i bbox = this.selectRowBackgroundBox(false, firstLine, lastLine);
            this.blit(guiGraphics, offsetX, currentY, bbox);
            if (scrollLevel + i < this.rows.size()) {
                FilterTermScreen.Row row = this.rows.get(scrollLevel + i);
                if (row instanceof FilterTermScreen.SlotsRow slotsRow) {
                    bbox = this.selectRowBackgroundBox(true, firstLine, lastLine);
                    bbox.setWidth(8 + 18 * slotsRow.slots - 1);
                    this.blit(guiGraphics, offsetX, currentY, bbox);
                }
            }

            currentY += 18;
        }

    }

    private Rect2i selectRowBackgroundBox(boolean isInvLine, boolean firstLine, boolean lastLine) {
        if (isInvLine) {
            if (firstLine) {
                return ROW_INVENTORY_TOP_BBOX;
            } else {
                return lastLine ? ROW_INVENTORY_BOTTOM_BBOX : ROW_INVENTORY_MIDDLE_BBOX;
            }
        } else if (firstLine) {
            return ROW_TEXT_TOP_BBOX;
        } else {
            return lastLine ? ROW_TEXT_BOTTOM_BBOX : ROW_TEXT_MIDDLE_BBOX;
        }
    }

    public boolean charTyped(char character, int key) {
        return character == ' ' && this.searchField.getValue().isEmpty() || super.charTyped(character, key);
    }

    public void clear() {
        this.byId.clear();
        this.cachedSearches.clear();
        this.refreshList();
    }

    public void postFullUpdate(long inventoryId, long sortBy, FilterContainerGroup group, int inventorySize, Int2ObjectMap<ItemStack> slots) {
        FilterContainerRecord record = new FilterContainerRecord(inventoryId, inventorySize, sortBy, group);
        this.byId.put(inventoryId, record);
        AppEngInternalInventory inventory = record.getInventory();
        ObjectIterator var10 = slots.int2ObjectEntrySet().iterator();

        while(var10.hasNext()) {
            Int2ObjectMap.Entry<ItemStack> entry = (Int2ObjectMap.Entry)var10.next();
            inventory.setItemDirect(entry.getIntKey(), (ItemStack)entry.getValue());
        }

        this.cachedSearches.clear();
        this.refreshList();
    }

    public void postIncrementalUpdate(long inventoryId, Int2ObjectMap<ItemStack> slots) {
        FilterContainerRecord record = this.byId.get(inventoryId);
        if (record == null) {
            LOGGER.warn("Ignoring incremental update for unknown inventory id {}", inventoryId);
        } else {
            AppEngInternalInventory inventory = record.getInventory();
            ObjectIterator var6 = slots.int2ObjectEntrySet().iterator();

            while(var6.hasNext()) {
                Int2ObjectMap.Entry<ItemStack> entry = (Int2ObjectMap.Entry)var6.next();
                inventory.setItemDirect(entry.getIntKey(), (ItemStack)entry.getValue());
            }

        }
    }

    private void refreshList() {
        this.byGroup.clear();
        String searchFilterLowerCase = this.searchField.getValue().toLowerCase();
        Set<Object> cachedSearch = this.getCacheForSearchTerm(searchFilterLowerCase);
        boolean rebuild = cachedSearch.isEmpty();

        for(FilterContainerRecord entry : this.byId.values()) {
            if (rebuild || cachedSearch.contains(entry)) {
                boolean found = searchFilterLowerCase.isEmpty();
                if (!found) {
                    for(ItemStack itemStack : entry.getInventory()) {
                        found = this.itemStackMatchesSearchTerm(itemStack, searchFilterLowerCase);
                        if (found) {
                            break;
                        }
                    }
                }

                if (!found && !entry.getSearchName().contains(searchFilterLowerCase)) {
                    cachedSearch.remove(entry);
                } else {
                    this.byGroup.put(entry.getGroup(), entry);
                    cachedSearch.add(entry);
                }
            }
        }

        this.groups.clear();
        this.groups.addAll(this.byGroup.keySet());
        this.groups.sort(GROUP_COMPARATOR);
        this.rows.clear();
        this.rows.ensureCapacity(this.getMaxRows());

        for(FilterContainerGroup group : this.groups) {
            this.rows.add(new FilterTermScreen.GroupHeaderRow(group));
            ArrayList<FilterContainerRecord> containers = new ArrayList<>(this.byGroup.get(group));
            Collections.sort(containers);

            for(FilterContainerRecord container : containers) {
                AppEngInternalInventory inventory = container.getInventory();

                for(int offset = 0; offset < inventory.size(); offset += 9) {
                    int slots = Math.min(inventory.size() - offset, 9);
                    FilterTermScreen.SlotsRow containerRow = new FilterTermScreen.SlotsRow(container, offset, slots);
                    this.rows.add(containerRow);
                }
            }
        }

        this.resetScrollbar();
    }

    private void resetScrollbar() {
        this.scrollbar.setHeight(this.visibleRows * 18 - 2);
        this.scrollbar.setRange(0, this.rows.size() - this.visibleRows, 2);
    }

    private boolean itemStackMatchesSearchTerm(ItemStack itemStack, String searchTerm) {
        if (itemStack.isEmpty()) {
            return false;
        } else {
            CompoundTag encodedValue = itemStack.getTag();
            if (encodedValue == null) {
                return false;
            } else {
                ListTag outTag = encodedValue.getList("out", 10);

                for(int i = 0; i < outTag.size(); ++i) {
                    ItemStack parsedItemStack = ItemStack.of(outTag.getCompound(i));
                    AEItemKey itemKey = AEItemKey.of(parsedItemStack);
                    if (itemKey != null) {
                        String displayName = itemKey.getDisplayName().getString().toLowerCase();
                        if (displayName.contains(searchTerm)) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }
    }

    private Set<Object> getCacheForSearchTerm(String searchTerm) {
        if (!this.cachedSearches.containsKey(searchTerm)) {
            this.cachedSearches.put(searchTerm, new HashSet<>());
        }

        Set<Object> cache = this.cachedSearches.get(searchTerm);
        if (cache.isEmpty() && searchTerm.length() > 1) {
            cache.addAll(this.getCacheForSearchTerm(searchTerm.substring(0, searchTerm.length() - 1)));
        }

        return cache;
    }

    private void reinitialize() {
        this.children().removeAll(this.renderables);
        this.renderables.clear();
        this.init();
    }

    private void toggleTerminalStyle(SettingToggleButton<TerminalStyle> btn, boolean backwards) {
        TerminalStyle next = (TerminalStyle)btn.getNextValue(backwards);
        AEConfig.instance().setTerminalStyle(next);
        btn.set(next);
        this.reinitialize();
    }

    private int getMaxRows() {
        return this.groups.size() + this.byId.size();
    }

    private void blit(GuiGraphics guiGraphics, int offsetX, int offsetY, Rect2i srcRect) {
        ResourceLocation texture = AppEng.makeId("textures/guis/filter_terminal.png");
        guiGraphics.blit(texture, offsetX, offsetY, srcRect.getX(), srcRect.getY(), srcRect.getWidth(), srcRect.getHeight());
    }

    record GroupHeaderRow(FilterContainerGroup group) implements FilterTermScreen.Row {

        public FilterContainerGroup group() {
            return this.group;
        }
    }

    record SlotsRow(FilterContainerRecord container, int offset, int slots) implements FilterTermScreen.Row {

        public FilterContainerRecord container() {
            return this.container;
        }

        public int offset() {
            return this.offset;
        }

        public int slots() {
            return this.slots;
        }
    }

    sealed interface Row permits FilterTermScreen.GroupHeaderRow, FilterTermScreen.SlotsRow {
    }
}
