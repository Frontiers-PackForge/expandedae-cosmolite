package lu.kolja.expandedae.block.entity;

import appeng.api.inventories.ISegmentedInventory;
import appeng.api.inventories.InternalInventory;
import appeng.api.stacks.AEItemKey;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.upgrades.UpgradeInventories;
import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocator;
import lu.kolja.expandedae.block.ExpPatternProviderBlock;
import lu.kolja.expandedae.definition.ExpBlocks;
import lu.kolja.expandedae.definition.ExpMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExpPatternProviderBlockEntity extends PatternProviderBlockEntity implements IUpgradeableObject {
    private final IUpgradeInventory upgrades;

    public ExpPatternProviderBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);

        this.upgrades = UpgradeInventories.forMachine(ExpBlocks.EXP_PATTERN_PROVIDER, 4, this::saveChanges);
    }
    @Nullable
    @Override
    public InternalInventory getSubInventory(ResourceLocation id) {
        if (id.equals(ISegmentedInventory.UPGRADES)) return this.upgrades;
        return super.getSubInventory(id);
    }

    @Override
    public IUpgradeInventory getUpgrades() {
        return upgrades;
    }
    @Override
    public void addAdditionalDrops(Level level, BlockPos pos, List<ItemStack> drops) {
        super.addAdditionalDrops(level, pos, drops);

        for (var upgrade : upgrades) {
            drops.add(upgrade);
        }
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
