package lu.kolja.expandedae;

import com.mojang.logging.LogUtils;
import lu.kolja.expandedae.bootstrap.Proxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(Expandedae.MODID)
public class Expandedae {

    public static final String MODID = "expandedae";
    public static final Logger LOGGER = LogUtils.getLogger();

    //getActionableNode().getGrid().getStorageService().getInventory().insert() TODO IMPLEMENT TO STICKY CARD

    public Expandedae() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.register(ExpConfig.class);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ExpConfig.SPEC);

        DistExecutor.safeRunForDist(
                () -> Proxy.Client::new,
                () -> Proxy.Server::new
        ).init(modEventBus);
        var rarity = Rarity.UNCOMMON;

        switch (rarity) {
            case COMMON -> System.out.println("common");
            case UNCOMMON -> System.out.println("uncommon");
            case RARE -> System.out.println("rare");
            case EPIC -> System.out.println("epic");
            default -> throw new IllegalStateException("Unknown rarity");
        }
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation makeId(String path) {
        return new ResourceLocation(MODID, path);
    }
}