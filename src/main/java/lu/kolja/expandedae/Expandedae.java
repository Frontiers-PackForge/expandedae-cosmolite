package lu.kolja.expandedae;

import com.mojang.logging.LogUtils;
import lu.kolja.expandedae.client.ClientRegistryHandler;
import lu.kolja.expandedae.common.EAEItemAndBlock;
import lu.kolja.expandedae.common.EAERegistryHandler;
import lu.kolja.expandedae.common.me.taglist.TagExpParser;
import lu.kolja.expandedae.common.me.taglist.TagPriorityList;
import lu.kolja.expandedae.config.EAEConfig;
import lu.kolja.expandedae.network.EAENetworkHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(Expandedae.MODID)
public class Expandedae {

    public static final String MODID = "expandedae";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static Expandedae INSTANCE;

    public Expandedae() {
        assert INSTANCE == null;
        INSTANCE = this;
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EAEConfig.SPEC);
        EAEItemAndBlock.init(EAERegistryHandler.INSTANCE);
        bus.register(EAERegistryHandler.INSTANCE);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> bus.register(ClientRegistryHandler.INSTANCE));
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener((RegisterEvent e) -> {
            if (e.getRegistryKey() == Registries.CREATIVE_MODE_TAB) {
                EAERegistryHandler.INSTANCE.registerTab(e.getVanillaRegistry());
            }
        });
        MinecraftForge.EVENT_BUS.addListener(this::onTagUpdate);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        EAERegistryHandler.INSTANCE.onInit();
        EAENetworkHandler.INSTANCE.init();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        ClientRegistryHandler.INSTANCE.init();
    }

    public void onTagUpdate(TagsUpdatedEvent event) {
        TagExpParser.reset();
        TagPriorityList.reset();
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MODID, name);
    }
}
