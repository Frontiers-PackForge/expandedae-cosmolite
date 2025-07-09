package lu.kolja.expandedae.mixin.cpu;

import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.mixin.accessor.AccessorBuiltInModelHooks;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ModelBakery.class)
public abstract class MixinModelBakery {

    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    private void loadModelHook(ResourceLocation id, CallbackInfoReturnable<UnbakedModel> cir) {
        UnbakedModel model = expandedae$getUnbakedModel(id);
        if (model != null) {
            cir.setReturnValue(model);
        }
    }

    @Unique
    private UnbakedModel expandedae$getUnbakedModel(ResourceLocation variant) {
        if (!variant.getNamespace().equals(Expandedae.MODID)) return null;
        return AccessorBuiltInModelHooks.getBuiltInModels().get(variant);
    }
}
