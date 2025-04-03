package lu.kolja.expandedae.mixin.emi;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import appeng.client.gui.me.items.CraftingTermScreen;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.menu.slot.FakeSlot;
import appeng.menu.slot.RestrictedInputSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.EmiStackInteraction;
import dev.emi.emi.api.stack.FluidEmiStack;

@Mixin(value = EmiStackInteraction.class, remap = false)
public class MixinEmiStackInteraction {

    @Mutable
    @Shadow
    @Final
    private EmiIngredient stack;

    @Inject(
            method = "<init>(Ldev/emi/emi/api/stack/EmiIngredient;Ldev/emi/emi/api/recipe/EmiRecipe;Z)V",
            remap = false,
            at = @At("RETURN")
    )
    public void EmiStackInteraction(@NotNull EmiIngredient stack, EmiRecipe recipe, boolean clickable, CallbackInfo ci) {
        var stacks = stack.getEmiStacks().stream().filter(s -> s.getItemStack().getItem() instanceof BucketItem);
        System.out.println("meow");
        var screen = Minecraft.getInstance().screen;
        if (screen instanceof CraftingTermScreen<?> cScreen) {
            this.stack = FluidEmiStack.of((BucketItem) stacks);
            System.out.println(cScreen);
        }
        System.out.println(screen);
    }
}
