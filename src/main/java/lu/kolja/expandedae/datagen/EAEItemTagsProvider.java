package lu.kolja.expandedae.datagen;

import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.common.EAEItemAndBlock;
import lu.kolja.expandedae.util.EAETags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EAEItemTagsProvider extends ItemTagsProvider {

    public EAEItemTagsProvider(PackOutput p, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> block, @Nullable ExistingFileHelper existingFileHelper) {
        super(p, lookupProvider, block, Expandedae.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(EAETags.EXP_PATTERN_PROVIDER)
                .add(EAEItemAndBlock.EXP_PATTERN_PROVIDER_PART)
                .add(EAEItemAndBlock.EXP_PATTERN_PROVIDER.asItem());
    }
}
