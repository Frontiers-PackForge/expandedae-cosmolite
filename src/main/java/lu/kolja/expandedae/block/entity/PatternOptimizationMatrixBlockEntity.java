package lu.kolja.expandedae.block.entity;

import appeng.api.networking.GridFlags;
import appeng.blockentity.grid.AENetworkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PatternOptimizationMatrixBlockEntity extends AENetworkBlockEntity {
    public PatternOptimizationMatrixBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
        this.getMainNode().setFlags(GridFlags.REQUIRE_CHANNEL);
    }
}
