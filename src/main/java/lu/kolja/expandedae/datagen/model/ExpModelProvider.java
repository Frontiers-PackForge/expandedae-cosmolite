package lu.kolja.expandedae.datagen.model;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import appeng.block.networking.EnergyCellBlock;
import appeng.core.definitions.BlockDefinition;
import appeng.datagen.providers.models.AE2BlockStateProvider;
import appeng.init.client.InitItemModelsProperties;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.definition.ExpBlocks;
import lu.kolja.expandedae.enums.ExpTiers;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static lu.kolja.expandedae.definition.ExpItems.AUTO_COMPLETE_CARD;
import static lu.kolja.expandedae.definition.ExpItems.EXP_PATTERN_PROVIDER_UPGRADE;
import static lu.kolja.expandedae.definition.ExpItems.GREATER_ACCEL_CARD;
import static lu.kolja.expandedae.definition.ExpItems.PATTERN_REFILLER_CARD;

public class ExpModelProvider extends AE2BlockStateProvider {
    public ExpModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Expandedae.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // General
        basicItem(EXP_PATTERN_PROVIDER_UPGRADE);
        // CARDS
        basicItem(AUTO_COMPLETE_CARD);
        basicItem(PATTERN_REFILLER_CARD);
        basicItem(GREATER_ACCEL_CARD);
        energyCell(ExpBlocks.EXP_ENERGY_CELL, "block/exp_energy_cell");
        // CPU
        for (var cpu : ExpTiers.values()) {
            var block = cpu.getDefinition().block();
            var name = cpu.isCPU() ? cpu.getCpuAffix() : cpu.getAffix();
            var model = models().cubeAll("block/crafting/" + name, Expandedae.makeId("block/crafting/" + name));
            getVariantBuilder(block)
                    .partialState()
                    .with(AbstractCraftingUnitBlock.FORMED, false)
                    .setModels(new ConfiguredModel(model))
                    .partialState()
                    .with(AbstractCraftingUnitBlock.FORMED, true)
                    .setModels(new ConfiguredModel(models().getBuilder("block/crafting/" + name + "_formed")));
            simpleBlockItem(block, model);
        }

    }

    private void basicItem(ItemLike item) {
        itemModels().basicItem(item.asItem());
    }

    @NotNull
    @Override
    public String getName() {
        return "Block States / Models";
    }

    /**
     * Code borrowed from AE2's {@link appeng.datagen.providers.models.BlockModelProvider#energyCell(BlockDefinition, String)}
     */
    private void energyCell(
            BlockDefinition<?> block,
            String baseTexture) {

        var blockBuilder = getVariantBuilder(block.block());
        var models = new ArrayList<ModelFile>();
        for (var i = 0; i < 5; i++) {
            var model = models().cubeAll(modelPath(block) + "_" + i, Expandedae.makeId(baseTexture + "_" + i));
            blockBuilder.partialState().with(EnergyCellBlock.ENERGY_STORAGE, i).setModels(new ConfiguredModel(model));
            models.add(model);
        }

        var item = itemModels().withExistingParent(modelPath(block), models.get(0).getLocation());
        for (var i = 1; i < models.size(); i++) {
            // The predicate matches "greater than", meaning for fill-level > 0 the first non-empty texture is used
            float fillFactor = i / (float) models.size();
            item.override()
                    .predicate(InitItemModelsProperties.ENERGY_FILL_LEVEL_ID, fillFactor)
                    .model(models.get(i));
        }
    }

    private String modelPath(BlockDefinition<?> block) {
        return block.id().getPath();
    }
}
