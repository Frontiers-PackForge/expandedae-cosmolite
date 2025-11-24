package lu.kolja.expandedae.mixin.terminal;

import appeng.api.config.ActionItems;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.menu.me.items.PatternEncodingTermMenu;
import lu.kolja.expandedae.helper.patternprovider.IPatternEncodingTerminalMenu;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PatternEncodingTermScreen.class, remap = false)
public class MixinPatternEncodingTermScreen {

    @Inject(
            method = "lambda$new$1",
            at = @At("TAIL")
    )
    private static void encode(PatternEncodingTermMenu menu, ActionItems act, CallbackInfo ci) {
        ((IPatternEncodingTerminalMenu) menu).eae$MovePattern(Screen.hasShiftDown());
    }
}
