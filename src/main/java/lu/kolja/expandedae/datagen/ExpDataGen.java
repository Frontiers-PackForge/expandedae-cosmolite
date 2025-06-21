package lu.kolja.expandedae.datagen;

import lu.kolja.expandedae.Expandedae;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Expandedae.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExpDataGen {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var out = gen.getPackOutput();
        var existing = event.getExistingFileHelper();

        gen.addProvider(event.includeClient(), new ExpLangProvider(out));
        gen.addProvider(event.includeClient(), new ExpModelProvider(out, existing));
        gen.addProvider(event.includeServer(), new ExpRecipeProvider(out));
        gen.addProvider(event.includeServer(), new ExpLootProvider(out));
    }
}
