package lu.kolja.expandedae.mixin.storage;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import appeng.client.gui.me.crafting.CraftingStatusTableRenderer;
import appeng.menu.me.crafting.CraftingStatusEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

@Mixin(value = CraftingStatusTableRenderer.class, remap = false)
public abstract class MixinCraftingStatusTableRenderer {

    @Inject(method = "getEntryTooltip*", at = @At("RETURN"), cancellable = true)
    private void addTooltip(CraftingStatusEntry entry, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> tooltip = cir.getReturnValue();
        tooltip.add(Component.literal("Click to find pattern provider").withStyle(ChatFormatting.GRAY));
        cir.setReturnValue(tooltip);
    }
}
