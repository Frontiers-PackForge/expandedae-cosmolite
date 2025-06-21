package lu.kolja.expandedae.mixin.cpu;

import appeng.me.cluster.implementations.CraftingCPUCluster;
import lu.kolja.expandedae.helper.misc.Maths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = CraftingCPUCluster.class, priority = 2000,remap = false)
public class MixinCraftingCPUCluster {
    @ModifyConstant(method = "addBlockEntity", constant = @Constant(intValue = 16), remap = false)
    private int modifyThreadLimit(int constant) {
        return Maths.pow(2, 20);
    }
}
