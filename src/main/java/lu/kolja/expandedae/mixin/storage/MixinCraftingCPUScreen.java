package lu.kolja.expandedae.mixin.storage;

import java.util.Arrays;

import lu.kolja.expandedae.helper.IPatternProviderFinder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import appeng.api.networking.IGrid;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.StackWithBounds;
import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.me.crafting.CraftingCPUScreen;
import appeng.client.gui.me.crafting.CraftingStatusScreen;
import appeng.client.gui.me.crafting.CraftingStatusTableRenderer;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.crafting.CraftingStatusMenu;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

@Mixin(value = AEBaseScreen.class)
public abstract class MixinCraftingCPUScreen<T extends AEBaseMenu> extends AbstractContainerScreen<T>{

    public MixinCraftingCPUScreen(T p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        // Only handle shift-clicks
        if (!Screen.hasShiftDown()) {
            return;
        }

        // Only handle in CraftingStatusScreen
        AEBaseScreen<?> screen = (AEBaseScreen<?>)(Object)this;
        if (!(screen instanceof CraftingStatusScreen)) {
            return;
        }

        CraftingStatusScreen craftingScreen = (CraftingStatusScreen)screen;
        CraftingStatusMenu menu = craftingScreen.getMenu();

        // Get the clicked item
        StackWithBounds hoveredStack = craftingScreen.getStackUnderMouse(mouseX, mouseY);
        if (hoveredStack == null || hoveredStack.stack() == null) {
            return;
        }
        this.
        // Get the grid from the menu
        IGrid grid = menu.getGrid();
        if (grid == null) {
            return;
        }

        // Find pattern providers with this pattern
        for (var machine : grid.getMachines(PatternProviderBlockEntity.class)) {
            PatternProviderBlockEntity provider = (PatternProviderBlockEntity)machine;
            BlockPos pos = provider.getBlockPos();

            for (var pattern : provider.getLogic().getAvailablePatterns()) {
                if (Arrays.stream(pattern.getOutputs()).anyMatch(output -> output.what().equals(hoveredStack.stack().what()))) {
                    // Found a match! Send coordinates to player
                    screen.getMinecraft().player.sendSystemMessage(Component.literal(
                            String.format("§aPattern Provider found at: §e%d, %d, %d",
                                    pos.getX(), pos.getY(), pos.getZ())
                    ));
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }
}
