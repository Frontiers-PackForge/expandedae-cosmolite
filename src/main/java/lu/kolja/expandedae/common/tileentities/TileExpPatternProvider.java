package lu.kolja.expandedae.common.tileentities;

import appeng.api.stacks.AEItemKey;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocator;
import com.glodblock.github.glodium.util.GlodUtil;
import lu.kolja.expandedae.common.EAEItemAndBlock;
import lu.kolja.expandedae.common.container.ContainerExpPatternProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class TileExpPatternProvider extends PatternProviderBlockEntity {
    public TileExpPatternProvider(BlockPos pos, BlockState blockState) {
        super(GlodUtil.getTileType(TileExpPatternProvider.class, TileExpPatternProvider::new, EAEItemAndBlock.EXP_PATTERN_PROVIDER), pos, blockState);
    }

    @Override
    protected PatternProviderLogic createLogic() {
        return new PatternProviderLogic(this.getMainNode(), this, 72);
    }
    @Override
    public void openMenu(Player player, MenuLocator locator) {
        MenuOpener.open(ContainerExpPatternProvider.TYPE, player, locator);
    }
    @Override
    public void returnToMainMenu(Player player, ISubMenu subMenu) {
        MenuOpener.returnTo(ContainerExpPatternProvider.TYPE, player, subMenu.getLocator());
    }
    @Override
    public ItemStack getMainMenuIcon() {
        return new ItemStack(EAEItemAndBlock.EXP_PATTERN_PROVIDER);
    }
    @Override
    public AEItemKey getTerminalIcon() {
        return AEItemKey.of(EAEItemAndBlock.EXP_PATTERN_PROVIDER);
    }
}
