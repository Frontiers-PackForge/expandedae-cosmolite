package lu.kolja.expandedae.mixin.misc;

import appeng.api.config.Setting;
import appeng.api.config.Settings;
import lu.kolja.expandedae.definition.ExpSettings;
import lu.kolja.expandedae.enums.BlockingMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Settings.class)
public class MixinSettings {
    @SafeVarargs
    @Shadow(remap = false)
    private static <T extends Enum<T>> Setting<T> register(String name, T firstOption, T... moreOptions) {
        return null;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void init(CallbackInfo ci) {
        ExpSettings.BLOCKING_MODE = register("blocking_type", BlockingMode.ALL, BlockingMode.DEFAULT, BlockingMode.SMART);
    }
}
