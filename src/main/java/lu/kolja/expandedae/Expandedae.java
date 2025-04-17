package lu.kolja.expandedae;

import lu.kolja.expandedae.definition.*;
import lu.kolja.expandedae.menu.ExpPatternProviderMenu;
import lu.kolja.expandedae.menu.FilterTermMenu;
import lu.kolja.expandedae.screen.FilterTermScreen;
import lu.kolja.expandedae.xmod.XMod;
import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.init.client.InitScreens;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Expandedae.MODID)
public class Expandedae {

    public static final String MODID = "expandedae";
    public static final Logger LOGGER = LogUtils.getLogger();

    //getActionableNode().getGrid().getStorageService().getInventory().insert() TODO IMPLEMENT TO STICKY CARD

    public Expandedae() {
        registerMenus();
        initResources();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);
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
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation makeId(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static void initResources() {
        ExpBlocks.init();
        ExpItems.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        new XMod();
        new ExpUpgrades(event);
    }

    private void registerMenus() {
        Platform.registerMenuType("filter_terminal", FilterTermMenu.TYPE);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        InitScreens.register(
                FilterTermMenu.TYPE,
                FilterTermScreen<FilterTermMenu>::new,
                "/screens/filter_terminal.json"
        );
        InitScreens.register(
                ExpMenus.EXP_PATTERN_PROVIDER,
                PatternProviderScreen<ExpPatternProviderMenu>::new,
                "/screens/exp_pattern_provider.json"
        );
    }
}