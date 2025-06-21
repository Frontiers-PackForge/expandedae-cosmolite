package lu.kolja.expandedae.datagen;

import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.definition.ExpBlocks;
import lu.kolja.expandedae.definition.ExpItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ExpLangProvider extends LanguageProvider {
    public ExpLangProvider(PackOutput packOutput) {
        super(packOutput, Expandedae.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        for (var item : ExpItems.getItems()) {
            add(item.asItem(), item.getEnglishName());
        }

        for (var block : ExpBlocks.getBlocks()) {
            add(block.block(), block.getEnglishName());
        }

        for (var cpu : ExpBlocks.getCPUs().values()) {
            add(cpu.block(), cpu.getEnglishName());
        }
    }

    @Override
    public String getName() {
        return "Language";
    }
}