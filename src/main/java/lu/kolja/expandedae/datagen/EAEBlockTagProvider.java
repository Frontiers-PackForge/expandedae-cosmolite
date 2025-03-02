package lu.kolja.expandedae.datagen;

import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.api.ISpecialDrop;
import lu.kolja.expandedae.common.EAERegistryHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EAEBlockTagProvider extends BlockTagsProvider {
    public EAEBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Expandedae.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        TagKey<Block> pickaxe = BlockTags.MINEABLE_WITH_PICKAXE;
        for (var block : EAERegistryHandler.INSTANCE.getBlocks()) {
            if (!(block instanceof ISpecialDrop)) {
                tag(pickaxe).add(block);
            }
        }
    }
}
