package lu.kolja.expandedae.definition;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.core.definitions.BlockDefinition;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.block.entity.ExpPatternProviderBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ExpBlockEntities {
    private static final Map<ResourceLocation, BlockEntityType<?>> BLOCK_ENTITY_TYPES = new HashMap<>();
    public static final BlockEntityType<ExpPatternProviderBlockEntity> EXP_PATTERN_PROVIDER = create(
            "exp_pattern_provider",
            ExpPatternProviderBlockEntity.class,
            ExpPatternProviderBlockEntity::new,
            ExpBlocks.EXP_PATTERN_PROVIDER);

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
