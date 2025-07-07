package lu.kolja.expandedae.definition;

import appeng.block.AEBaseBlockItem;
import appeng.block.crafting.CraftingUnitBlock;
import appeng.core.definitions.BlockDefinition;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.block.block.ExpIOPortBlock;
import lu.kolja.expandedae.block.block.ExpPatternProviderBlock;
import lu.kolja.expandedae.block.item.ExpIOPortBlockItem;
import lu.kolja.expandedae.block.item.ExpPatternProviderBlockItem;
import lu.kolja.expandedae.enums.ExpTiers;
import lu.kolja.expandedae.item.misc.ExpCPUItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ExpBlocks {

    private static final List<BlockDefinition<?>> BLOCKS = new ArrayList<>();

    public static final BlockDefinition<ExpPatternProviderBlock> EXP_PATTERN_PROVIDER = block(
            "Expanded Pattern Provider",
            "exp_pattern_provider",
            ExpPatternProviderBlock::new,
            ExpPatternProviderBlockItem::new
    );
    public static final BlockDefinition<ExpIOPortBlock> EXP_IO_PORT = block(
            "Expanded IO Port",
            "exp_io_port",
            ExpIOPortBlock::new,
            ExpIOPortBlockItem::new
    );

    public static final BlockDefinition<CraftingUnitBlock> EXP_CRAFTING_UNIT = block(
            "Expanded Crafting Unit",
            "exp_crafting_unit",
            () -> new CraftingUnitBlock(ExpTiers.UNIT),
            AEBaseBlockItem::new
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_2 = cpu(
            "2x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_2",
            ExpTiers.TIER_2,
            () -> EXP_CRAFTING_UNIT
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_4 = cpu(
            "4x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_4",
            ExpTiers.TIER_4,
            () -> CPU_2
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_8 = cpu(
            "8x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_8",
            ExpTiers.TIER_8,
            () -> CPU_4
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_16 = cpu(
            "16x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_16",
            ExpTiers.TIER_16,
            () -> CPU_8
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_32 = cpu(
            "32x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_32",
            ExpTiers.TIER_32,
            () -> CPU_16
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_64 = cpu(
            "64x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_64",
            ExpTiers.TIER_64,
            () -> CPU_32
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_128 = cpu(
            "128x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_128",
            ExpTiers.TIER_128,
            () -> CPU_64
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_256 = cpu(
            "256x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_256",
            ExpTiers.TIER_256,
            () -> CPU_128
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_512 = cpu(
            "512x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_512",
            ExpTiers.TIER_512,
            () -> CPU_256
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_1K = cpu(
            "1K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_1k",
            ExpTiers.TIER_1K,
            () -> CPU_512
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_2K = cpu(
            "2K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_2k",
            ExpTiers.TIER_2K,
            () -> CPU_1K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_4K = cpu(
            "4K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_4k",
            ExpTiers.TIER_4K,
            () -> CPU_2K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_8K = cpu(
            "8K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_8k",
            ExpTiers.TIER_8K,
            () -> CPU_4K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_16K = cpu(
            "16K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_16k",
            ExpTiers.TIER_16K,
            () -> CPU_8K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_32K = cpu(
            "32K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_32k",
            ExpTiers.TIER_32K,
            () -> CPU_16K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_64K = cpu(
            "64K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_64k",
            ExpTiers.TIER_64K,
            () -> CPU_32K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_128K = cpu(
            "128K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_128k",
            ExpTiers.TIER_128K,
            () -> CPU_64K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_256K = cpu(
            "256K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_256k",
            ExpTiers.TIER_256K,
            () -> CPU_128K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_512K = cpu(
            "512K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_512k",
            ExpTiers.TIER_512K,
            () -> CPU_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_1M = cpu(
            "1M Crafting Co-Processing Unit",
            "exp_crafting_accelerator_1m",
            ExpTiers.TIER_1M,
            () -> CPU_512K
    );
    public static BlockDefinition<CraftingUnitBlock> cpu(
            String englishName, String id, ExpTiers cpu, Supplier<ItemLike> disassemblyExtra
    ) {
        return block(
                englishName, id,
                () -> new CraftingUnitBlock(cpu),
                (block, props) -> new ExpCPUItem(block, props, disassemblyExtra)
        );
    }

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
