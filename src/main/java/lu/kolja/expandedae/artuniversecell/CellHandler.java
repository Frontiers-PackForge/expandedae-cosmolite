package lu.kolja.expandedae.artuniversecell;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import lu.kolja.expandedae.item.misc.ArtUniverseCellItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CellHandler implements ICellHandler {
    public static final CellHandler INSTANCE = new CellHandler();

    @Override
    public boolean isCell(ItemStack is) {
        return is.getItem() instanceof ArtUniverseCellItem;
    }

    @Override
    public @Nullable CellInventory getCellInventory(ItemStack itemStack, @Nullable ISaveProvider iSaveProvider) {
        return CellInventory.createInventory(itemStack, iSaveProvider, 63);
    }
}
