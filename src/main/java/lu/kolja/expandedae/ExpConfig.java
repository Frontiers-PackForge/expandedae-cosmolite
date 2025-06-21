package lu.kolja.expandedae;

import lu.kolja.expandedae.helper.misc.Maths;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class ExpConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.IntValue IDLE_DRAIN = BUILDER
            .comment("Cell idle drain: how much AE/t it uses passively.")
            .defineInRange("idle_drain", 512, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue MAX_THREADS = BUILDER
            .comment("Up to which exponent of 2 should co-processors be created")
            .comment("For example, at the default value of 20 it would enable co-processors with threads from 2^1 up to 2^20")
            .comment("Set to 0 to disable")
            .defineInRange("max_threads", 20, 0, 20);
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int idleDrain;
    public static int maxThreadsPow;
    public static int maxThreads = Maths.pow(2, maxThreadsPow);

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        idleDrain = IDLE_DRAIN.get();
        maxThreadsPow = MAX_THREADS.get();
    }
}
