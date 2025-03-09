package lu.kolja.expandedae.definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.block.ExpPatternProviderBlock;
import lu.kolja.expandedae.block.ExpPatternProviderBlockItem;
import appeng.core.definitions.BlockDefinition;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ExpBlocks {

    private static final List<BlockDefinition<?>> BLOCKS = new ArrayList<>();
    public static final BlockDefinition<ExpPatternProviderBlock> EXP_PATTERN_PROVIDER = block(
            "Expanded Pattern Provider",
            "exp_pattern_provider",
            ExpPatternProviderBlock::new,
            ExpPatternProviderBlockItem::new
    );

    public static void init() {
        // controls static load order
        Expandedae.LOGGER.info("Initialised blocks.");
    }

    public static List<BlockDefinition<?>> getBlocks() {
        return Collections.unmodifiableList(BLOCKS);
    }

    public static <T extends Block> BlockDefinition<T> block(
            String englishName,
            String id,
            Supplier<T> blockSupplier,
            BiFunction<Block, Item.Properties, BlockItem> itemFactory) {
        var block = blockSupplier.get();
        var item = itemFactory.apply(block, new Item.Properties());

        var definition = new BlockDefinition<>(englishName, Expandedae.makeId(id), block, item);
        BLOCKS.add(definition);
        return definition;
    }

}
