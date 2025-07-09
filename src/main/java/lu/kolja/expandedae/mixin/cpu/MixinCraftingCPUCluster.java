package lu.kolja.expandedae.mixin.cpu;

import appeng.blockentity.crafting.CraftingBlockEntity;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = CraftingCPUCluster.class, priority = 2000, remap = false)
public class MixinCraftingCPUCluster {
    @Redirect(
            method = "addBlockEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/blockentity/crafting/CraftingBlockEntity;getAcceleratorThreads()I",
                    ordinal = 1
            ),
            remap = false
    )
    private int modifyThreadLimit(CraftingBlockEntity instance) {
        return 1; //so any number of threads is allowed
    }
}
