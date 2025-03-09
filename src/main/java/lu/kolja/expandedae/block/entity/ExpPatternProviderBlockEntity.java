package lu.kolja.expandedae.block.entity;

import lu.kolja.expandedae.block.ExpPatternProviderBlock;
import lu.kolja.expandedae.definition.ExpBlocks;
import lu.kolja.expandedae.definition.ExpMenus;
import appeng.api.stacks.AEItemKey;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.upgrades.UpgradeInventories;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ExpPatternProviderBlockEntity extends PatternProviderBlockEntity implements IUpgradeableObject {

    private IUpgradeInventory inv;

    public ExpPatternProviderBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
        inv = UpgradeInventories.forMachine(ExpBlocks.EXP_PATTERN_PROVIDER, 4, this::saveChanges);
    }

    @Override
    public PatternProviderLogic createLogic() {
        return ExpPatternProviderBlock.createLogic(this.getMainNode(), this);
    }

    @Override
    public void openMenu(Player player, MenuLocator locator) {
        MenuOpener.open(ExpMenus.EXP_PATTERN_PROVIDER, player, locator);
    }

    @Override
    public void returnToMainMenu(Player player, ISubMenu subMenu) {
        MenuOpener.returnTo(ExpMenus.EXP_PATTERN_PROVIDER, player, subMenu.getLocator());
    }

    @Override
    public AEItemKey getTerminalIcon() {
        return AEItemKey.of(ExpBlocks.EXP_PATTERN_PROVIDER.stack());
    }

    @Override
    public ItemStack getMainMenuIcon() {
        return ExpBlocks.EXP_PATTERN_PROVIDER.stack();
    }
}