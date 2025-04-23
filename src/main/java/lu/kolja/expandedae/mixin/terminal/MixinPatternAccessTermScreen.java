package lu.kolja.expandedae.mixin.terminal;

import com.glodblock.github.extendedae.client.gui.widget.AssemblerMatrixSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.patternaccess.PatternAccessTermScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.crafting.pattern.ProcessingPatternItem;
import appeng.menu.SlotSemantics;
import appeng.menu.implementations.PatternAccessTermMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@Mixin(value = PatternAccessTermScreen.class, remap = false)
public abstract class MixinPatternAccessTermScreen<C extends PatternAccessTermMenu> extends AEBaseScreen<C> {

    public MixinPatternAccessTermScreen(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "slotClicked", at = @At("RETURN"))
    private void slotClicked(Slot slot, int slotIdx, int mouseButton, ClickType clickType, CallbackInfo ci) {
        var menu = (PatternAccessTermMenu) this.menu;
        if (clickType == ClickType.QUICK_MOVE) canPlace(slot, menu);
    }
    private void canPlace(Slot clickedSlot, PatternAccessTermMenu menu) {


        for (var slot1 : menu.getSlots(SlotSemantics.ENCODED_PATTERN)) {
            if (clickedSlot.getItem().getItem() instanceof EncodedPatternItem patternItem) {
                if (patternItem instanceof ProcessingPatternItem && !(slot1 instanceof AssemblerMatrixSlot)) {
                    if (!slot1.hasItem()) {
                        slot1.set(clickedSlot.getItem());
                        clickedSlot.set(ItemStack.EMPTY);
                        return;
                    }
                } else {
                    if (!slot1.hasItem()) {
                        slot1.set(clickedSlot.getItem());
                        clickedSlot.set(ItemStack.EMPTY);
                        return;
                    }
                }
            }
        }
    }
}
