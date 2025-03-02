package lu.kolja.expandedae.common;

import appeng.items.parts.PartItem;
import lu.kolja.expandedae.common.blocks.BlockExpPatternProvider;
import lu.kolja.expandedae.common.parts.PartExpPatternProvider;
import lu.kolja.expandedae.common.tileentities.TileExpPatternProvider;
import net.minecraft.world.item.Item;

public class EAEItemAndBlock {

    public static BlockExpPatternProvider EXP_PATTERN_PROVIDER;
    public static PartItem<PartExpPatternProvider> EXP_PATTERN_PROVIDER_PART;

    public static void init(EAERegistryHandler registryHandler) {
        EXP_PATTERN_PROVIDER = new BlockExpPatternProvider();
        EXP_PATTERN_PROVIDER_PART = new PartItem<>(new Item.Properties(), PartExpPatternProvider.class, PartExpPatternProvider::new);
        registryHandler.block("exp_pattern_provider", EXP_PATTERN_PROVIDER, TileExpPatternProvider.class, TileExpPatternProvider::new);
        registryHandler.item("exp_pattern_provider_part", EXP_PATTERN_PROVIDER_PART);
    }

}
