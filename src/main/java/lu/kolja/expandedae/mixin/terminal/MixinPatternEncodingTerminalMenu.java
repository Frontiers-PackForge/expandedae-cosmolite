package lu.kolja.expandedae.mixin.terminal;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

import lu.kolja.expandedae.helper.KeybindUtil;
import com.mojang.blaze3d.platform.InputConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import appeng.helpers.IMenuCraftingPacket;
import appeng.menu.me.items.PatternEncodingTermMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(value = PatternEncodingTermMenu.class, remap = false)
public abstract class MixinPatternEncodingTerminalMenu implements IMenuCraftingPacket {
    @Inject(method = "encodePattern", at = @At("RETURN"))
    private void encodePattern(CallbackInfoReturnable<ItemStack> cir) {
        if (!KeybindUtil.isShiftDown()) return;
        AtomicReference<Player> player = new AtomicReference<>();
        this.getActionSource().player().ifPresent(player::set);
        if (player.get().getInventory().add(cir.getReturnValue())) {
            player.get().drop(cir.getReturnValue(), false);
        }
    }
}
