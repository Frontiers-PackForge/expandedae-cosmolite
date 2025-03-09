package lu.kolja.expandedae.xmod.extendedae;

import lu.kolja.expandedae.definition.ExpBlocks;
import lu.kolja.expandedae.definition.ExpItems;
import com.glodblock.github.extendedae.common.EPPItemAndBlock;
import com.glodblock.github.extendedae.common.items.ItemMEPackingTape;
import appeng.api.upgrades.Upgrades;

public class ExtendedAE {
    public ExtendedAE() {
        ItemMEPackingTape.registerPackableDevice(ExpBlocks.EXP_PATTERN_PROVIDER.id());
        ItemMEPackingTape.registerPackableDevice(ExpItems.EXP_PATTERN_PROVIDER_PART.id());

        Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, EPPItemAndBlock.EX_PATTERN_PROVIDER, 1, "group.ex_pattern_provider.name");
        Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, EPPItemAndBlock.EX_PATTERN_PROVIDER_PART, 1, "group.ex_pattern_provider.name");
    }
}