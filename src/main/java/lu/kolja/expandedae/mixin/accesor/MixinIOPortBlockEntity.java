package lu.kolja.expandedae.mixin.accesor;

import appeng.api.networking.IGrid;
import appeng.api.storage.cells.StorageCell;
import appeng.blockentity.storage.IOPortBlockEntity;
import appeng.util.inv.AppEngInternalInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = IOPortBlockEntity.class, remap = false)
public interface MixinIOPortBlockEntity {
    @Accessor("inputCells")
    AppEngInternalInventory getInputCells();

    @Invoker("moveSlot")
    boolean invokeMoveSlot(int x);

    @Invoker("transferContents")
    long invokeTransferContents(IGrid grid, StorageCell cellInv, long itemsToMove);
}
