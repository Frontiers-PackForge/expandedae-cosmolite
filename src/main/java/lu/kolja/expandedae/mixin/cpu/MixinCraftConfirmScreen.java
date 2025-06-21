package lu.kolja.expandedae.mixin.cpu;

import appeng.client.gui.me.crafting.CraftConfirmScreen;
import lu.kolja.expandedae.helper.misc.NumberUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.text.NumberFormat;

@Mixin(value = CraftConfirmScreen.class, remap = false)
public class MixinCraftConfirmScreen {
    @Redirect(
            method = "updateBeforeRender",
            at = @At(value = "INVOKE", target = "Ljava/text/NumberFormat;format(J)Ljava/lang/String;")
    )
    private String formatStorage(NumberFormat instance, long number) {
        return NumberUtil.formatNum(number);
    }
}
