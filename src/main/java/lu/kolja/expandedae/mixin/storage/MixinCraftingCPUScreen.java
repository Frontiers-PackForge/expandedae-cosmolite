package lu.kolja.expandedae.mixin.storage;

import java.util.Arrays;

import lu.kolja.expandedae.helper.ICPUAccessor;
import lu.kolja.expandedae.helper.IPatternProviderFinder;
import org.spongepowered.asm.mixin.Mixin;
import appeng.api.networking.IGrid;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.client.gui.StackWithBounds;
import appeng.client.gui.me.crafting.CraftingCPUScreen;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.crafting.CraftingStatusMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

@Mixin(value = CraftingCPUScreen.class)
public abstract class MixinCraftingCPUScreen<T extends AEBaseMenu> extends Screen{

    protected MixinCraftingCPUScreen(Component p_96550_) {
        super(p_96550_);
    }

    @Override
    public boolean mouseClicked(double xCoord, double yCoord, int btn) {
        // Only handle shift-clicks
        if (!Screen.hasShiftDown()) {
            return super.mouseClicked(xCoord, yCoord, btn);
        }

        // Only handle in CraftingStatusScreen
        var screen = Minecraft.getInstance().screen;
        if (!(screen instanceof CraftingCPUScreen<?> craftScreen)) {
            return super.mouseClicked(xCoord, yCoord, btn);
        }

        CraftingStatusMenu menu = (CraftingStatusMenu) craftScreen.getMenu();

        // Get the clicked item
        StackWithBounds hoveredStack = craftScreen.getStackUnderMouse(xCoord, yCoord);
        if (hoveredStack == null || hoveredStack.stack() == null) {
            return super.mouseClicked(xCoord, yCoord, btn);
        }

        ((IPatternProviderFinder) menu).findPatternProvider(hoveredStack.stack());
        // Get the grid from the menu
        //AtomicReference<IGrid> grid2 = new AtomicReference<>();
        //craftScreen.getMenu().getActionSource().machine().ifPresent(it -> grid2.set(it.getActionableNode().getGrid()));
        //var grid3 = grid2.get();
        IGrid grid = ((ICPUAccessor) menu).getGrid();
        if (grid == null) {
            return super.mouseClicked(xCoord, yCoord, btn);
        }

        // Find pattern providers with this pattern
        for (var machine : grid.getMachines(PatternProviderBlockEntity.class)) {
            BlockPos pos = machine.getBlockPos();

            for (var pattern : machine.getLogic().getAvailablePatterns()) {
                if (Arrays.stream(pattern.getOutputs()).anyMatch(output -> output.what().equals(hoveredStack.stack().what()))) {
                    // Found a match! Send coordinates to player
                    craftScreen.getMinecraft().player.sendSystemMessage(Component.literal(
                            String.format("§aPattern Provider found at: §e%d, %d, %d",
                                    pos.getX(), pos.getY(), pos.getZ())
                    ));
                    return super.mouseClicked(xCoord, yCoord, btn);
                }
            }
        }
        return super.mouseClicked(xCoord, yCoord, btn);
    }
}