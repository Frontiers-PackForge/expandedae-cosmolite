package lu.kolja.expandedae;

import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.init.client.InitScreens;
import com.mojang.logging.LogUtils;
import lu.kolja.expandedae.definition.*;
import lu.kolja.expandedae.menu.ExpPatternProviderMenu;
import lu.kolja.expandedae.xmod.ExtendedAE;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(Expandedae.MODID)
public class Expandedae {

    public static final String MODID = "expandedae";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation makeId(String path) {
        return new ResourceLocation(MODID, path);
    }

    public Expandedae() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey().equals(Registries.BLOCK)) {
                ExpBlocks.getBlocks().forEach(b -> {
                    ForgeRegistries.BLOCKS.register(b.id(), b.block());
                    ForgeRegistries.ITEMS.register(b.id(), b.asItem());
                });
            }
            if (event.getRegistryKey().equals(Registries.ITEM)) {
                ExpItems.getItems().forEach(i -> ForgeRegistries.ITEMS.register(i.id(), i.asItem()));
            }
            if (event.getRegistryKey().equals(Registries.BLOCK_ENTITY_TYPE)) {
                ExpBlockEntities.getBlockEntityTypes().forEach(ForgeRegistries.BLOCK_ENTITY_TYPES::register);
            }
            if (event.getRegistryKey().equals(Registries.CREATIVE_MODE_TAB)) {
                Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ExpCreativeTab.ID, ExpCreativeTab.TAB);
            }
            if (event.getRegistryKey().equals(Registries.MENU)) {
                ExpMenus.getMenuTypes().forEach(ForgeRegistries.MENU_TYPES::register);
            }
        });
        initResources();
        //initXMod();
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        InitScreens.register(
                ExpMenus.EXP_PATTERN_PROVIDER,
                PatternProviderScreen<ExpPatternProviderMenu>::new,
                "/screens/exp_pattern_provider.json"
        );
    }

    public static void initXMod() {
        if (ModList.get().isLoaded("expatternprovider")) {
            LOGGER.debug("ExtendedAE found, initializing xmod!");
            new ExtendedAE();
        }
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MODID, name);
    }

    public static void initResources() {
        ExpBlocks.init();
    }
}