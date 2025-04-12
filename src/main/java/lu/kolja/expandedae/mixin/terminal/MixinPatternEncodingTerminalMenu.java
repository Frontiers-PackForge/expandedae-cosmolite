package lu.kolja.expandedae.mixin.terminal;

import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import appeng.helpers.IMenuCraftingPacket;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.slot.RestrictedInputSlot;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(value = PatternEncodingTermMenu.class, remap = false)
public abstract class MixinPatternEncodingTerminalMenu implements IMenuCraftingPacket {
    @Final
    @Shadow @Mutable
    private RestrictedInputSlot encodedPatternSlot;

    @Inject(method = "encode", at = @At("RETURN"))
    private void encode(CallbackInfo ci) {
        if (!ContainerScreen.hasShiftDown()) return;
        if (encodedPatternSlot.getItem() != ItemStack.EMPTY) {
            AtomicReference<Player> player = new AtomicReference<>();
            this.getActionSource().player().ifPresent(player::set);
            if (player.get().getInventory().items.stream().filter(i -> !i.equals(ItemStack.EMPTY)).toList().size() >= 36) return; //this because Inventory#add does not work for whatever reason
            player.get().getInventory().add(encodedPatternSlot.getItem());
            encodedPatternSlot.set(ItemStack.EMPTY);
            encodedPatternSlot.setChanged();
        }
    }
}