package lu.kolja.expandedae.datagen;

import appeng.api.util.AEColor;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEParts;
import com.glodblock.github.extendedae.common.EPPItemAndBlock;
import gripe._90.megacells.definition.MEGABlocks;
import gripe._90.megacells.definition.MEGAItems;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.datagen.conditionals.ModNotLoadedCondition;
import lu.kolja.expandedae.enums.ExpTiers;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static appeng.core.definitions.AEItems.*;
import static appeng.core.definitions.AEParts.PATTERN_PROVIDER;
import static lu.kolja.expandedae.definition.ExpBlocks.*;
import static lu.kolja.expandedae.definition.ExpItems.*;
import static lu.kolja.expandedae.enums.Addons.EXT;
import static lu.kolja.expandedae.enums.Addons.MEGA;
import static lu.kolja.expandedae.enums.ExpTiers.*;
import static net.minecraft.data.recipes.RecipeCategory.MISC;

public class ExpRecipeProvider extends RecipeProvider {
    public ExpRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> out) {
        ShapedRecipeBuilder.shaped(MISC, EXP_PATTERN_PROVIDER)
                .pattern("ECE")
                .pattern("CPC")
                .pattern("ECE")
                .define('C', CAPACITY_CARD)
                .define('E', ENGINEERING_PROCESSOR)
                .define('P', PATTERN_PROVIDER)
                .unlockedBy("has_engineering_processor", has(ENGINEERING_PROCESSOR))
                .unlockedBy("has_pattern_provider", has(PATTERN_PROVIDER))
                .save(out, craftingId("exp_pattern_provider"));
        ShapelessRecipeBuilder.shapeless(MISC, EXP_PATTERN_PROVIDER)
                .requires(EXP_PATTERN_PROVIDER_PART)
                .unlockedBy("has_exp_pattern_provider_part", has(EXP_PATTERN_PROVIDER_PART))
                .save(out, craftingId("exp_pattern_provider_alt"));
        ShapelessRecipeBuilder.shapeless(MISC, EXP_PATTERN_PROVIDER_PART)
                .requires(EXP_PATTERN_PROVIDER)
                .unlockedBy("has_exp_pattern_provider", has(EXP_PATTERN_PROVIDER))
                .save(out, craftingId("exp_pattern_provider_part"));

        ShapedRecipeBuilder.shaped(MISC, EXP_PATTERN_PROVIDER_UPGRADE)
                .pattern("ECE")
                .pattern("C C")
                .pattern("ECE")
                .define('C', CAPACITY_CARD)
                .define('E', ENGINEERING_PROCESSOR)
                .unlockedBy("has_engineering_processor", has(ENGINEERING_PROCESSOR))
                .unlockedBy("has_capacity_card", has(CAPACITY_CARD))
                .save(out, craftingId("exp_pattern_provider_upgrade"));

        ShapelessRecipeBuilder.shapeless(MISC, AUTO_COMPLETE_CARD)
                .requires(ADVANCED_CARD)
                .requires(CRAFTING_CARD)
                .unlockedBy("has_advanced_card", has(ADVANCED_CARD))
                .unlockedBy("has_crafting_card", has(CRAFTING_CARD))
                .save(out, craftingId("auto_complete_card"));

        ShapedRecipeBuilder.shaped(MISC, PATTERN_REFILLER_CARD)
                .pattern("AB")
                .pattern("BE")
                .define('A', ADVANCED_CARD)
                .define('B', BLANK_PATTERN)
                .define('E', ENGINEERING_PROCESSOR)
                .unlockedBy("has_advanced_card", has(ADVANCED_CARD))
                .unlockedBy("has_engineering_processor", has(ENGINEERING_PROCESSOR))
                .save(out, craftingId("pattern_refiller_card"));

        ConditionalRecipe.builder()
                .addCondition(new ModNotLoadedCondition(MEGA.mod))
                .addRecipe(ShapedRecipeBuilder.shaped(MISC, EXP_CRAFTING_UNIT)
                        .pattern("UPU")
                        .pattern("CLC")
                        .pattern("UPU")
                        .define('U', AEBlocks.CRAFTING_UNIT)
                        .define('P', ENGINEERING_PROCESSOR)
                        .define('C', AEParts.SMART_DENSE_CABLE.item(AEColor.TRANSPARENT))
                        .define('L', LOGIC_PROCESSOR)
                        .unlockedBy("has_engineering_processor", has(ENGINEERING_PROCESSOR))
                        .unlockedBy("has_logic_processor", has(LOGIC_PROCESSOR)) ::save)
                .build(out, craftingId("exp_crafting_unit_ae"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition(MEGA.mod))
                .addRecipe(ShapedRecipeBuilder.shaped(MISC, EXP_CRAFTING_UNIT)
                        .pattern("UPU")
                        .pattern("CLC")
                        .pattern("UPU")
                        .define('U', MEGABlocks.MEGA_CRAFTING_UNIT)
                        .define('P', ENGINEERING_PROCESSOR)
                        .define('C', AEParts.SMART_DENSE_CABLE.item(AEColor.TRANSPARENT))
                        .define('L', MEGAItems.ACCUMULATION_PROCESSOR)
                        .unlockedBy("has_engineering_processor", has(ENGINEERING_PROCESSOR))
                        .unlockedBy("has_logic_processor", has(MEGAItems.ACCUMULATION_PROCESSOR)) ::save)
                .build(out, craftingId("exp_crafting_unit_mega"));

        ConditionalRecipe.builder()
                .addCondition(new ModNotLoadedCondition(EXT.mod))
                .addRecipe(ShapedRecipeBuilder.shaped(MISC, EXP_IO_PORT)
                        .pattern("AAA")
                        .pattern("AIA")
                        .pattern("AAA")
                        .define('A', SPEED_CARD)
                        .define('I', AEBlocks.IO_PORT)
                        .unlockedBy("has_speed_card", has(SPEED_CARD)) ::save)
                .build(out, craftingId("exp_io_port_ae"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition(EXT.mod))
                .addRecipe(ShapedRecipeBuilder.shaped(MISC, EXP_IO_PORT)
                        .pattern("AAA")
                        .pattern("AIA")
                        .pattern("AAA")
                        .define('A', SPEED_CARD)
                        .define('I', EPPItemAndBlock.EX_IO_PORT)
                        .unlockedBy("has_speed_card", has(SPEED_CARD)) ::save)
                .build(out, craftingId("exp_io_port_ext"));

        ShapelessRecipeBuilder.shapeless(MISC, TIER_2.getDefinition())
                .requires(EXP_CRAFTING_UNIT)
                .requires(ENGINEERING_PROCESSOR)
                .unlockedBy("has_engineering_processor", has(ENGINEERING_PROCESSOR))
                .save(out, craftingId("exp_crafting_accelerator_2"));

        upgrade(out, TIER_2, TIER_4);
        upgrade(out, TIER_4, TIER_8);
        upgrade(out, TIER_8, TIER_16);
        upgrade(out, TIER_16, TIER_32);
        upgrade(out, TIER_32, TIER_64);
        upgrade(out, TIER_64, TIER_128);
        upgrade(out, TIER_128, TIER_256);
        upgrade(out, TIER_256, TIER_512);
        upgrade(out, TIER_512, TIER_1K);
        upgrade(out, TIER_1K, TIER_2K);
        upgrade(out, TIER_2K, TIER_4K);
        upgrade(out, TIER_4K, TIER_8K);
        upgrade(out, TIER_8K, TIER_16K);
        upgrade(out, TIER_16K, TIER_32K);
        upgrade(out, TIER_32K, TIER_64K);
        upgrade(out, TIER_64K, TIER_128K);
        upgrade(out, TIER_128K, TIER_256K);
        upgrade(out, TIER_256K, TIER_512K);
        upgrade(out, TIER_512K, TIER_1M);
    }

    private void upgrade(Consumer<FinishedRecipe> out, ExpTiers previousTier, ExpTiers tier) {
        ShapelessRecipeBuilder.shapeless(MISC, tier.getDefinition())
                .requires(previousTier.getDefinition(), 2)
                .unlockedBy("has_" + previousTier.getAffix(), has(previousTier.getDefinition()))
                .save(out, craftingId("exp_crafting_accelerator_" + tier.getAffix()));
    }

    private static void conditional(RecipeBuilder recipe, Consumer<FinishedRecipe> out, ICondition condition, ResourceLocation id) {
        recipe.save(finished -> {
            ConditionalRecipe
                    .builder()
                    .addRecipe(finished)
                    .addCondition(condition)
                    .build(out, id);
        });
    }


    private static ICondition loaded(String mod) {
        return new ModLoadedCondition(mod);
    }
    private static ICondition notLoaded(String mod) {
        return new ModNotLoadedCondition(mod);
    }

    private ResourceLocation craftingId(String name) {
        return Expandedae.makeId("crafting/" + name);
    }
}
