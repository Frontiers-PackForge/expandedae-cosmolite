package lu.kolja.expandedae.datagen;

import lu.kolja.expandedae.Expandedae;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Expandedae.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EAEDataGen {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent dataEvent) {
        var pack = dataEvent.getGenerator().getVanillaPack(true);
        var file = dataEvent.getExistingFileHelper();
        var lookup = dataEvent.getLookupProvider();
        var blockTagsProvider = pack
                .addProvider(c -> new EAEBlockTagProvider(c, lookup, file));
        pack.addProvider(EAERecipeProvider::new);
        pack.addProvider(EAELootTableProvider::new);
        pack.addProvider(c -> new EAEItemTagsProvider(c, lookup, blockTagsProvider.contentsGetter(), file));
    }

}
