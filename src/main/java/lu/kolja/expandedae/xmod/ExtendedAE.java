package lu.kolja.expandedae.xmod;

import com.glodblock.github.extendedae.common.items.ItemMEPackingTape;
import lu.kolja.expandedae.definition.ExpBlocks;
import lu.kolja.expandedae.definition.ExpItems;

public class ExtendedAE {
    public ExtendedAE() {
        ItemMEPackingTape.registerPackableDevice(ExpBlocks.EXP_PATTERN_PROVIDER.id());
        ItemMEPackingTape.registerPackableDevice(ExpItems.EXP_PATTERN_PROVIDER_UPGRADE.id());
    }
}