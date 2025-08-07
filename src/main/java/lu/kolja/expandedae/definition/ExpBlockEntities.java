package lu.kolja.expandedae.definition;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.crafting.CraftingBlockEntity;
import appeng.core.definitions.BlockDefinition;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.block.entity.ExpIOPortBlockEntity;
import lu.kolja.expandedae.block.entity.ExpPatternProviderBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ExpBlockEntities {
    private static final Map<ResourceLocation, BlockEntityType<?>> BLOCK_ENTITY_TYPES = new HashMap<>();
    public static final BlockEntityType<ExpPatternProviderBlockEntity> EXP_PATTERN_PROVIDER = create(
            "exp_pattern_provider",
            ExpPatternProviderBlockEntity.class,
            ExpPatternProviderBlockEntity::new,
            ExpBlocks.EXP_PATTERN_PROVIDER
    );
    public static final BlockEntityType<ExpIOPortBlockEntity> EXP_IO_PORT = create(
            "exp_io_port",
            ExpIOPortBlockEntity.class,
            ExpIOPortBlockEntity::new,
            ExpBlocks.EXP_IO_PORT
    );
    public static final BlockEntityType<CraftingBlockEntity> EXP_CPUS = create(
            "exp_cpus",
            CraftingBlockEntity.class,
            CraftingBlockEntity::new,
            ExpBlocks.EXP_CRAFTING_UNIT,
            ExpBlocks.CPU_2,
            ExpBlocks.CPU_4,
            ExpBlocks.CPU_8,
            ExpBlocks.CPU_16,
            ExpBlocks.CPU_32,
            ExpBlocks.CPU_64,
            ExpBlocks.CPU_128,
            ExpBlocks.CPU_256,
            ExpBlocks.CPU_512,
            ExpBlocks.CPU_1K,
            ExpBlocks.CPU_2K,
            ExpBlocks.CPU_4K,
            ExpBlocks.CPU_8K,
            ExpBlocks.CPU_16K,
            ExpBlocks.CPU_32K,
            ExpBlocks.CPU_64K,
            ExpBlocks.CPU_128K,
            ExpBlocks.CPU_256K,
            ExpBlocks.CPU_512K,
            ExpBlocks.CPU_1M
    );

    public static Map<ResourceLocation, BlockEntityType<?>> getBlockEntityTypes() {
        return Collections.unmodifiableMap(BLOCK_ENTITY_TYPES);
    }

    @SafeVarargs
    public static <T extends AEBaseBlockEntity> BlockEntityType<T> create(
            String id,
            Class<T> entityClass,
            BlockEntityFactory<T> factory,
            BlockDefinition<? extends AEBaseEntityBlock<?>>... blockDefinitions) {
        if (blockDefinitions.length == 0) {
            throw new IllegalArgumentException();
        }

        var blocks = Arrays.stream(blockDefinitions).map(BlockDefinition::block).toArray(AEBaseEntityBlock[]::new);

        var typeHolder = new AtomicReference<BlockEntityType<T>>();
        var type = BlockEntityType.Builder.of(
                        (blockPos, blockState) -> factory.create(typeHolder.get(), blockPos, blockState), blocks)
                .build(null);
        typeHolder.set(type);
        BLOCK_ENTITY_TYPES.put(Expandedae.makeId(id), type);

        AEBaseBlockEntity.registerBlockEntityItem(type, blockDefinitions[0].asItem());

        for (var block : blocks) {
            var baseBlock = (AEBaseEntityBlock<T>) block;
            baseBlock.setBlockEntity(entityClass, type, null, null);
        }

        return type;
    }

    public interface BlockEntityFactory<T extends AEBaseBlockEntity> {
        T create(BlockEntityType<T> type, BlockPos pos, BlockState state);
    }
}
