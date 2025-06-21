package lu.kolja.expandedae.definition;

import appeng.block.crafting.CraftingUnitBlock;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.BlockDefinition;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.block.block.ExpIOPortBlock;
import lu.kolja.expandedae.block.block.ExpPatternProviderBlock;
import lu.kolja.expandedae.block.item.ExpIOPortBlockItem;
import lu.kolja.expandedae.block.item.ExpPatternProviderBlockItem;
import lu.kolja.expandedae.enums.ExpCraftingCPU;
import lu.kolja.expandedae.item.dummy.DummyCPU;
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
    private static final Map<ExpCraftingCPU,BlockDefinition<CraftingUnitBlock>> CPUS = new HashMap<>();

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

    public static BlockDefinition<CraftingUnitBlock> CPU_2 = cpu(
            "2x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_2",
            ExpCraftingCPU.THREADS_2,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_4 = cpu(
            "4x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_4",
            ExpCraftingCPU.THREADS_4,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_8 = cpu(
            "8x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_8",
            ExpCraftingCPU.THREADS_8,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_16 = cpu(
            "16x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_16",
            ExpCraftingCPU.THREADS_16,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_32 = cpu(
            "32x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_32",
            ExpCraftingCPU.THREADS_32,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_64 = cpu(
            "64x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_64",
            ExpCraftingCPU.THREADS_64,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_128 = cpu(
            "128x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_128",
            ExpCraftingCPU.THREADS_128,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_256 = cpu(
            "256x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_256",
            ExpCraftingCPU.THREADS_256,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_512 = cpu(
            "512x Crafting Co-Processing Unit",
            "exp_crafting_accelerator_512",
            ExpCraftingCPU.THREADS_512,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_1K = cpu(
            "1K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_1k",
            ExpCraftingCPU.THREADS_1K,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_2K = cpu(
            "2K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_2k",
            ExpCraftingCPU.THREADS_2K,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_4K = cpu(
            "4K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_4k",
            ExpCraftingCPU.THREADS_4K,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_8K = cpu(
            "8K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_8k",
            ExpCraftingCPU.THREADS_8K,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_16K = cpu(
            "16K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_16k",
            ExpCraftingCPU.THREADS_16K,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_32K = cpu(
            "32K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_32k",
            ExpCraftingCPU.THREADS_32K,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_64K = cpu(
            "64K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_64k",
            ExpCraftingCPU.THREADS_64K,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_128K = cpu(
            "128K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_128k",
            ExpCraftingCPU.THREADS_128K,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_256K = cpu(
            "256K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_256k",
            ExpCraftingCPU.THREADS_256K,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_512K = cpu(
            "512K Crafting Co-Processing Unit",
            "exp_crafting_accelerator_512k",
            ExpCraftingCPU.THREADS_512K,
            () -> AEItems.CELL_COMPONENT_256K
    );
    public static BlockDefinition<CraftingUnitBlock> CPU_1M = cpu(
            "1M Crafting Co-Processing Unit",
            "exp_crafting_accelerator_1m",
            ExpCraftingCPU.THREADS_1M,
            () -> AEItems.CELL_COMPONENT_256K
    );

    public static BlockDefinition<CraftingUnitBlock> cpu(
            String englishName, String id, ExpCraftingCPU cpu, Supplier<ItemLike> disassemblyExtra
    ) {
        var def = block(
                englishName, id, true,
                () -> new CraftingUnitBlock(cpu),
                (block, props) -> cpu.isEnabled()
                        ? new ExpCPUItem(block, props, disassemblyExtra)
                        : new DummyCPU(block, props)
        );
        CPUS.put(cpu, def);
        return def;
    }

    public static void init() {
        // controls static load order
        Expandedae.LOGGER.info("Initialised blocks.");
    }

    public static List<BlockDefinition<?>> getBlocks() {
        return Collections.unmodifiableList(BLOCKS);
    }
    public static Map<ExpCraftingCPU,BlockDefinition<CraftingUnitBlock>> getCPUs() {
        return Collections.unmodifiableMap(CPUS);
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
    public static <T extends Block> BlockDefinition<T> block(
            String englishName,
            String id,
            boolean addToTab,
            Supplier<T> blockSupplier,
            BiFunction<Block, Item.Properties, BlockItem> itemFactory) {
        var block = blockSupplier.get();
        var item = itemFactory.apply(block, new Item.Properties());

        return new BlockDefinition<>(englishName, Expandedae.makeId(id), block, item);
    }
}
