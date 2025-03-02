package lu.kolja.expandedae.config;

import lu.kolja.expandedae.Expandedae;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Expandedae.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EAEConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
