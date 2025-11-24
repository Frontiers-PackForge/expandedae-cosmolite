package lu.kolja.expandedae.xmod.megacells;

import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.ItemDefinition;
import appeng.items.materials.MaterialItem;
import gripe._90.megacells.definition.MEGABlocks;
import gripe._90.megacells.definition.MEGAItems;
import lu.kolja.expandedae.cell.dual.DualStorageCell;
import lu.kolja.expandedae.definition.ExpItems;

public class MegaCells {
    public MegaCells() {
        Upgrades.add(AEItems.AUTO_COMPLETE_CARD, MEGABlocks.MEGA_PATTERN_PROVIDER.asItem(), 1, "group.mega_pattern_provider.name");
        Upgrades.add(AEItems.AUTO_COMPLETE_CARD, MEGAItems.MEGA_PATTERN_PROVIDER.asItem(), 1, "group.mega_pattern_provider.name");
    }
    public static ItemDefinition<DualStorageCell> DUAL_1M;
    public static ItemDefinition<DualStorageCell> DUAL_4M;
    public static ItemDefinition<DualStorageCell> DUAL_16M;
    public static ItemDefinition<DualStorageCell> DUAL_64M;
    public static ItemDefinition<DualStorageCell> DUAL_256M;
    public static ItemDefinition<MaterialItem> DUAL_CELL_MEGA_HOUSING;

    public static void initItems() {
        DUAL_CELL_MEGA_HOUSING = ExpItems.item("ME MEGA Dual Cell Housing", "mega_dual_cell_housing", MaterialItem::new);

        DUAL_1M = ExpItems.dualCell("1m",
                MEGAItems.CELL_COMPONENT_1M, DUAL_CELL_MEGA_HOUSING, 6
        );
        DUAL_4M = ExpItems.dualCell("4m",
                MEGAItems.CELL_COMPONENT_4M, DUAL_CELL_MEGA_HOUSING, 7
        );
        DUAL_16M = ExpItems.dualCell("16m",
                MEGAItems.CELL_COMPONENT_16M, DUAL_CELL_MEGA_HOUSING, 8
        );
        DUAL_64M = ExpItems.dualCell("64m",
                MEGAItems.CELL_COMPONENT_64M, DUAL_CELL_MEGA_HOUSING, 9
        );
        DUAL_256M = ExpItems.dualCell("256m",
                MEGAItems.CELL_COMPONENT_256M, DUAL_CELL_MEGA_HOUSING, 10
        );
    }
}
