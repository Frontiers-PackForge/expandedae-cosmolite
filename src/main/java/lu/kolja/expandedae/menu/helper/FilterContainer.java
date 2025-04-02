package lu.kolja.expandedae.menu.helper;

import lu.kolja.expandedae.block.entity.FilterContainerGroup;
import org.jetbrains.annotations.Nullable;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGrid;

public interface FilterContainer {
    @Nullable IGrid getGrid();

    InternalInventory getFilteredInventory();

    default long getTerminalSortOrder() {
        return 0L;
    }

    FilterContainerGroup getTerminalGroup();
}
