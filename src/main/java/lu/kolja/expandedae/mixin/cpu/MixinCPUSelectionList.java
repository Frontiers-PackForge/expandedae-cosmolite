package lu.kolja.expandedae.mixin.cpu;

import appeng.client.Point;
import appeng.client.gui.widgets.CPUSelectionList;
import lu.kolja.expandedae.helper.cpu.ISearchScreen;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CPUSelectionList.class, remap = false)
public class MixinCPUSelectionList {
    @Inject(
            method = "onMouseUp",
            at = @At("RETURN")
    )
    private void eae$onMouseUp(Point mousePos, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        assert Minecraft.getInstance().screen != null;
        ((ISearchScreen) Minecraft.getInstance().screen).eae$clearSearch();
    }
}
