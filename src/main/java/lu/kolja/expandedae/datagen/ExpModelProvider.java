package lu.kolja.expandedae.datagen;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import appeng.datagen.providers.models.AE2BlockStateProvider;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.enums.ExpTiers;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

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
        basicItem(PATTERN_REFILLER_CARD);
        basicItem(GREATER_ACCEL_CARD);
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
}
