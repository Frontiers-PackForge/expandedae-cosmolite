package lu.kolja.expandedae.datagen;

import appeng.core.definitions.AEItems;
import appeng.datagen.providers.tags.ConventionTags;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.common.EAEItemAndBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class EAERecipeProvider extends RecipeProvider {

    static String C = "has_item";

    public EAERecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, EAEItemAndBlock.EXP_PATTERN_PROVIDER)
                .pattern("ECE")
                .pattern("CPC")
                .pattern("ECE")
                .define('P', ConventionTags.PATTERN_PROVIDER)
                .define('C', AEItems.CAPACITY_CARD)
                .define('E', AEItems.ENGINEERING_PROCESSOR)
                .unlockedBy(C, has(EAEItemAndBlock.EXP_PATTERN_PROVIDER))
                .save(consumer, Expandedae.id("exp"));
        ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, EAEItemAndBlock.EXP_PATTERN_PROVIDER_PART)
                .requires(EAEItemAndBlock.EXP_PATTERN_PROVIDER)
                .unlockedBy(C, has(EAEItemAndBlock.EXP_PATTERN_PROVIDER_PART))
                .save(consumer, Expandedae.id("epp_part"));
        ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, EAEItemAndBlock.EXP_PATTERN_PROVIDER)
                .requires(EAEItemAndBlock.EXP_PATTERN_PROVIDER_PART)
                .unlockedBy(C, has(EAEItemAndBlock.EXP_PATTERN_PROVIDER))
                .save(consumer, Expandedae.id("epp_alt"));
    }
}
