package lu.kolja.expandedae.helper;

import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGrid;
import lu.kolja.expandedae.block.entity.FilterContainerGroup;
import org.jetbrains.annotations.Nullable;

public interface FilterContainer {
    @Nullable IGrid getGrid();

    InternalInventory getFilteredInventory();

    default long getTerminalSortOrder() {
        return 0L;
    }

    FilterContainerGroup getTerminalGroup();
}
