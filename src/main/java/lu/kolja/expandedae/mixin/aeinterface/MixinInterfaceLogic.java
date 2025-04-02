package lu.kolja.expandedae.mixin.aeinterface;

import lu.kolja.expandedae.definition.ExpItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.helpers.InterfaceLogic;

@Mixin(targets = "appeng.helpers.InterfaceLogic$InterfaceInventory", remap = false)
public class MixinInterfaceLogic {
    @Shadow @Final
    InterfaceLogic this$0;

    @Inject(method = "insert", at = @At("HEAD"), cancellable = true)
    public void insert(AEKey what, long amount, Actionable mode, IActionSource source, CallbackInfoReturnable<Long> cir) {
        if (this$0.getUpgrades().isInstalled(ExpItems.ADVANCED_BLOCKING_CARD)
                && !this$0.getActionableNode().getGrid().getStorageService().getInventory().getAvailableStacks().isEmpty()) cir.cancel();

    }
}