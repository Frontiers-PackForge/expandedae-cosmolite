package lu.kolja.expandedae.bootstrap;

import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.render.crafting.CraftingCubeModel;
import appeng.hooks.BuiltInModelHooks;
import appeng.init.client.InitScreens;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.client.render.ExpCraftingUnitModelProvider;
import lu.kolja.expandedae.datagen.conditionals.ModNotLoadedCondition;
import lu.kolja.expandedae.definition.*;
import lu.kolja.expandedae.enums.ExpTiers;
import lu.kolja.expandedae.menu.ExpPatternProviderMenu;
import lu.kolja.expandedae.screen.ExpIOPortScreen;
import lu.kolja.expandedae.xmod.XMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public interface Proxy {
    class Server implements Proxy {
        public void init(IEventBus modEventBus) {
            ExpBlocks.init();
            ExpItems.init();

            modEventBus.addListener(this::commonSetup);
            modEventBus.addListener((RegisterEvent event) -> {
                event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS,
                        helper -> CraftingHelper.register(ModNotLoadedCondition.Serializer.INSTANCE)
                );
                if (event.getRegistryKey().equals(Registries.BLOCK)) {
                    ExpBlocks.getBlocks().forEach(def -> {
                        ForgeRegistries.BLOCKS.register(def.id(), def.block());
                        ForgeRegistries.ITEMS.register(def.id(), def.asItem());
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

        private void commonSetup(final FMLCommonSetupEvent event) {
            new XMod();
            new ExpUpgrades(event);
        }
    }

    class Client implements Proxy {
        public void init(IEventBus modEventBus) {
            new Server().init(modEventBus);
            modEventBus.addListener(this::clientSetup);
        }

        private void clientSetup(FMLClientSetupEvent event) {
            InitScreens.register(
                    ExpMenus.EXP_PATTERN_PROVIDER,
                    PatternProviderScreen<ExpPatternProviderMenu>::new,
                    "/screens/exp_pattern_provider.json"
            );
            InitScreens.register(
                    ExpMenus.EXP_IO_PORT,
                    ExpIOPortScreen::new,
                    "/screens/exp_io_port.json"
            );
            initCraftingUnitModels();
        }

        private static void initCraftingUnitModels() {
            for (var tier : ExpTiers.values()) {
                var affix = tier.isCPU() ? tier.getCpuAffix() : tier.getAffix();
                BuiltInModelHooks.addBuiltInModel(
                        Expandedae.makeId("block/crafting/" + affix + "_formed"),
                        new CraftingCubeModel(new ExpCraftingUnitModelProvider(tier))
                );
            }
        }
    }

    void init(IEventBus bus);
}
