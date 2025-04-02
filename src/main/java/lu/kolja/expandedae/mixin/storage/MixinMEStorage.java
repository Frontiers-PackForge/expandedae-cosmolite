package lu.kolja.expandedae.mixin.storage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.storage.MEStorage;

@Mixin(value = MEStorage.class, remap = false)
public class MixinMEStorage {
    @Inject(
            method = "insert",
            at = @At("HEAD"),
            cancellable = true
    )
    public void insert(AEKey what, long amount, Actionable mode, IActionSource source, CallbackInfoReturnable<Long> cir) {

    }
}
