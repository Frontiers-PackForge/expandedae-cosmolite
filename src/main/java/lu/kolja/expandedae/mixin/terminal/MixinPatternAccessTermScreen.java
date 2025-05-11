package lu.kolja.expandedae.mixin.terminal;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import appeng.client.gui.me.patternaccess.PatternSlot;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.Scrollbar;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.InventoryActionPacket;
import appeng.helpers.InventoryAction;
import appeng.menu.implementations.PatternAccessTermMenu;
import lu.kolja.expandedae.helper.ISlot;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PatternAccessTermScreen.class, remap = false)
public class MixinPatternAccessTermScreen<C extends PatternAccessTermMenu> extends AEBaseScreen<C> {

    @Final
    @Shadow private Scrollbar scrollbar;

    @Shadow private int visibleRows;

    public MixinPatternAccessTermScreen(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "slotClicked", at = @At("HEAD"))
    protected void slotClicked(Slot slot, int slotIdx, int mouseButton, ClickType clickType, CallbackInfo ci) {
        // Handle shift-click from player inventory to terminal
        if (clickType == ClickType.QUICK_MOVE && mouseButton == 0) {
            // Check if the clicked slot is in the player's inventory
            if (!(slot.container instanceof PatternSlot)) {
                ItemStack stack = slot.getItem();
                // Only proceed if it's a pattern
                if (!stack.isEmpty() && PatternDetailsHelper.isEncodedPattern(stack)) {
                    // Find the first visible empty pattern slot in the terminal
                    for (Slot terminalSlot : this.menu.slots) {
                        if (terminalSlot instanceof PatternSlot machineSlot &&
                                terminalSlot.getItem().isEmpty() &&
                                expandedae$isSlotVisible(terminalSlot)) {

                            var packet = new InventoryActionPacket(InventoryAction.SHIFT_CLICK,
                                    ((ISlot) machineSlot).expandedae$getSlot(),
                                    machineSlot.getMachineInv().getServerId()
                            );
                            NetworkHandler.instance().sendToServer(packet);
                        }
                    }
                }
            }
        }
        super.slotClicked(slot, slotIdx, mouseButton, clickType);
    }

    @Unique
    private boolean expandedae$isSlotVisible(Slot slot) {
        // Get the current scroll position
        int scrollOffset = scrollbar.getCurrentScroll();

        // Calculate the visible row range
        int startRow = scrollOffset;
        int endRow = startRow + visibleRows;

        // Get the slot's position
        int slotRow = slot.y / 18;

        // Check if the slot is within the visible range
        return slotRow >= startRow && slotRow < endRow;
    }
}
