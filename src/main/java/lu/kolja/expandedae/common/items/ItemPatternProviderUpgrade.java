package lu.kolja.expandedae.common.items;

import appeng.blockentity.crafting.PatternProviderBlockEntity;
import appeng.parts.crafting.PatternProviderPart;
import lu.kolja.expandedae.common.EAEItemAndBlock;
import lu.kolja.expandedae.common.tileentities.TileExpPatternProvider;
import net.minecraft.world.item.Item;

public class ItemPatternProviderUpgrade extends ItemUpgrade {
    public ItemPatternProviderUpgrade(Properties properties) {
        super(new Item.Properties());
        this.addTile(PatternProviderBlockEntity.class, EAEItemAndBlock.EXP_PATTERN_PROVIDER, TileExpPatternProvider.class);
        this.addPart(PatternProviderPart.class, EAEItemAndBlock.EXP_PATTERN_PROVIDER_PART);
    }
}
