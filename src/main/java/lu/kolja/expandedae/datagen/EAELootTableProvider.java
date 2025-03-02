package lu.kolja.expandedae.datagen;

import lu.kolja.expandedae.common.EAEItemAndBlock;
import lu.kolja.expandedae.common.EAERegistryHandler;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class EAELootTableProvider extends LootTableProvider {

    public EAELootTableProvider(PackOutput p) {
        super(p, Collections.emptySet(), Collections.singletonList(new LootTableProvider.SubProviderEntry(SubProvider::new, LootContextParamSets.BLOCK)));
    }

    public static class SubProvider extends BlockLootSubProvider {

        protected SubProvider() {
            super(Collections.emptySet(), FeatureFlagSet.of(), new HashMap<>());
        }

        @Override
        protected void generate() {
            for (var block : EAERegistryHandler.INSTANCE.getBlocks()) {
                add(block, createSingleItemTable(block));
            }
        }

        public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> bi) {
            this.generate();
            this.dropSelf(EAEItemAndBlock.EXP_PATTERN_PROVIDER);
            for (var e : this.map.entrySet()) {
                bi.accept(e.getKey(), e.getValue());
            }
        }
    }
}
