package lu.kolja.expandedae.block.block;

import appeng.api.orientation.IOrientationStrategy;
import appeng.api.orientation.OrientationStrategies;
import appeng.block.AEBaseEntityBlock;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.util.InteractionUtil;
import lu.kolja.expandedae.block.entity.ExpIOPortBlockEntity;
import lu.kolja.expandedae.definition.ExpMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ExpIOPortBlock extends AEBaseEntityBlock<ExpIOPortBlockEntity> {
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    public ExpIOPortBlock() {
        super(metalProps());
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        ExpIOPortBlockEntity be = this.getBlockEntity(level, pos);
        if (be != null) be.updateRedstoneState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    @Override
    public IOrientationStrategy getOrientationStrategy() {
        return OrientationStrategies.full();
    }

    @Override
    protected BlockState updateBlockStateFromBlockEntity(BlockState currentState, ExpIOPortBlockEntity be) {
        return currentState.setValue(POWERED, be.isActive());
    }

    @Override
    public InteractionResult onActivated(Level level, BlockPos pos, Player p, InteractionHand hand, @Nullable ItemStack heldItem, BlockHitResult hit) {
        if (InteractionUtil.isInAlternateUseMode(p)) {
            return InteractionResult.PASS;
        } else {
            ExpIOPortBlockEntity be = this.getBlockEntity(level, pos);
            if (be != null) {
                if (!level.isClientSide()) {
                    MenuOpener.open(ExpMenus.EXP_IO_PORT, p, MenuLocators.forBlockEntity(be));
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            } else {
                return InteractionResult.PASS;
            }
        }
    }
}
