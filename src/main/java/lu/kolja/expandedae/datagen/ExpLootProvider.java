package lu.kolja.expandedae.datagen;

import lu.kolja.expandedae.definition.ExpBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class ExpLootProvider extends LootTableProvider{
    public ExpLootProvider(PackOutput output) {
        super(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)));
    }

    private static class BlockLoot extends BlockLootSubProvider {
        protected BlockLoot() {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, new HashMap<>());
        }

        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> writer) {
            generate();
            map.forEach(writer);
        }

        @Override
        public void generate() {
            for (var block : ExpBlocks.getBlocks()) {
                add(block.block(),
                        LootTable.lootTable().withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(block))
                                .when(ExplosionCondition.survivesExplosion())
                        )
                );
            }
        }
    }
}
