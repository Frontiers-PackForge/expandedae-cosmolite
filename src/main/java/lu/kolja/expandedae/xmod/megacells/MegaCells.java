package lu.kolja.expandedae.xmod.megacells;

import appeng.api.upgrades.Upgrades;
import gripe._90.megacells.definition.MEGABlocks;
import gripe._90.megacells.definition.MEGAItems;
import lu.kolja.expandedae.definition.ExpItems;

public class MegaCells {
    public MegaCells() {
        Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, MEGABlocks.MEGA_PATTERN_PROVIDER.asItem(), 1, "group.mega_pattern_provider.name");
        Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, MEGAItems.MEGA_PATTERN_PROVIDER.asItem(), 1, "group.mega_pattern_provider.name");
    }
}
