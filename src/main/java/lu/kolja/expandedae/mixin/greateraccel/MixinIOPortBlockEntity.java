package lu.kolja.expandedae.mixin.greateraccel;

import lu.kolja.expandedae.definition.ExpItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.storage.StorageCells;
import appeng.api.storage.cells.StorageCell;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.util.IConfigurableObject;
import appeng.blockentity.grid.AENetworkInvBlockEntity;
import appeng.blockentity.storage.IOPortBlockEntity;
import appeng.core.definitions.AEItems;
import appeng.util.inv.AppEngInternalInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = IOPortBlockEntity.class, remap = false)
public abstract class MixinIOPortBlockEntity extends AENetworkInvBlockEntity
        implements IUpgradeableObject, IConfigurableObject, IGridTickable {
    @Shadow protected abstract boolean moveSlot(int x);

    @Shadow @Final private static int NUMBER_OF_CELL_SLOTS;

    @Shadow @Final private AppEngInternalInventory inputCells;

    @Shadow @Final private IUpgradeInventory upgrades;

    @Shadow protected abstract long transferContents(IGrid grid, StorageCell cellInv, long itemsToMove);

    @Shadow public abstract boolean matchesFullnessMode(StorageCell inv);

    public MixinIOPortBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    /**
     * @author Kolja
     * @reason speed
     */
    @Overwrite
    public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
        if (!this.getMainNode().isActive()) {
            return TickRateModulation.IDLE;
        }

        TickRateModulation ret = TickRateModulation.SLEEP;
        long itemsToMove = 256;

        switch (upgrades.getInstalledUpgrades(AEItems.SPEED_CARD)) {
            case 1 -> itemsToMove *= 2;
            case 2 -> itemsToMove *= 4;
            case 3 -> itemsToMove *= 8;
        }
        switch (upgrades.getInstalledUpgrades(ExpItems.GREATER_ACCEL_CARD)) {
            case 1 -> itemsToMove *= 4;
            case 2 -> itemsToMove *= 8;
            case 3 -> itemsToMove *= 16;
        }

        var grid = getMainNode().getGrid();
        if (grid == null) {
            return TickRateModulation.IDLE;
        }

        for (int x = 0; x < NUMBER_OF_CELL_SLOTS; x++) {
            var cell = this.inputCells.getStackInSlot(x);

            var cellInv = StorageCells.getCellInventory(cell, null);

            if (cellInv == null) {
                // This item is not a valid storage cell, try to move it to the output
                moveSlot(x);
                continue;
            }

            if (itemsToMove > 0) {
                itemsToMove = transferContents(grid, cellInv, itemsToMove);

                if (itemsToMove > 0) {
                    ret = TickRateModulation.IDLE;
                } else {
                    ret = TickRateModulation.URGENT;
                }
            }

            if (itemsToMove > 0 && matchesFullnessMode(cellInv) && this.moveSlot(x)) {
                ret = TickRateModulation.URGENT;
            }
        }

        return ret;
    }
}
