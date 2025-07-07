package lu.kolja.expandedae.block.entity;

import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.storage.StorageCells;
import appeng.api.upgrades.UpgradeInventories;
import appeng.blockentity.storage.IOPortBlockEntity;
import appeng.core.definitions.AEItems;
import appeng.util.inv.AppEngInternalInventory;
import lu.kolja.expandedae.definition.ExpBlocks;
import lu.kolja.expandedae.helper.misc.ExpReflection;
import lu.kolja.expandedae.mixin.accessor.AccessorIOPortBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ExpIOPortBlockEntity extends IOPortBlockEntity {
    private static final int NUMBER_OF_CELL_SLOTS = 6;
    private final AppEngInternalInventory inputCells;

    public ExpIOPortBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
        this.inputCells = ((AccessorIOPortBlockEntity) this).getInputCells();

        var field = ExpReflection.getField(IOPortBlockEntity.class, "upgrades");
        ExpReflection.setField(this, field, UpgradeInventories.forMachine(ExpBlocks.EXP_IO_PORT, 5, this::saveChanges));
    }

    @Override
    public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
        if (!this.getMainNode().isActive()) {
            return TickRateModulation.IDLE;
        }

        var ret = TickRateModulation.SLEEP;
        long itemsToMove = Integer.MAX_VALUE / 512;

        switch (this.getUpgrades().getInstalledUpgrades(AEItems.SPEED_CARD)) {
            case 1 -> itemsToMove *= 2;
            case 2 -> itemsToMove *= 8;
            case 3 -> itemsToMove *= 32;
            case 4 -> itemsToMove *= 128;
            case 5 -> itemsToMove *= 512;
        }

        var grid = getMainNode().getGrid();
        if (grid == null) {
            return TickRateModulation.IDLE;
        }

        for (int x = 0; x < NUMBER_OF_CELL_SLOTS; x++) {
            var cell = this.inputCells.getStackInSlot(x);
            var cellInv = StorageCells.getCellInventory(cell, null);

            if (cellInv == null) {
                ((AccessorIOPortBlockEntity) this).invokeMoveSlot(x);
                continue;
            }

            if (itemsToMove > 0) {
                itemsToMove = ((AccessorIOPortBlockEntity) this).invokeTransferContents(grid, cellInv, itemsToMove);

                if (itemsToMove > 0) {
                    ret = TickRateModulation.IDLE;
                } else {
                    ret = TickRateModulation.URGENT;
                }
            }

            if (itemsToMove > 0 && matchesFullnessMode(cellInv) && ((AccessorIOPortBlockEntity) this).invokeMoveSlot(x)) {
                ret = TickRateModulation.URGENT;
            }
        }
        return ret;
    }
}
