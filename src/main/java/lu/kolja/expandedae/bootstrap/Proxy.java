package lu.kolja.expandedae.bootstrap;

import appeng.api.implementations.blockentities.IChestOrDrive;
import appeng.api.storage.StorageCells;
import appeng.api.storage.cells.ICellGuiHandler;
import appeng.api.storage.cells.ICellHandler;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.render.crafting.CraftingCubeModel;
import appeng.hooks.BuiltInModelHooks;
import appeng.init.client.InitScreens;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.menu.me.common.MEStorageMenu;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.artuniversecell.CellHandler;
import lu.kolja.expandedae.artuniversecell.CellSavedData;
import lu.kolja.expandedae.client.render.ExpCraftingUnitModelProvider;
import lu.kolja.expandedae.definition.*;
import lu.kolja.expandedae.enums.ExpCraftingCPU;
import lu.kolja.expandedae.item.misc.ArtUniverseCellItem;
import lu.kolja.expandedae.menu.ExpPatternProviderMenu;
import lu.kolja.expandedae.screen.ExpIOPortScreen;
import lu.kolja.expandedae.xmod.XMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
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
            MinecraftForge.EVENT_BUS.addListener(Server::onLevelLoad);

            modEventBus.addListener(this::commonSetup);
            modEventBus.addListener((RegisterEvent event) -> {
                if (event.getRegistryKey().equals(Registries.BLOCK)) {
                    ExpBlocks.getBlocks().forEach(def -> {
                        ForgeRegistries.BLOCKS.register(def.id(), def.block());
                        ForgeRegistries.ITEMS.register(def.id(), def.asItem());
                    });
                    ExpBlocks.getCPUs().forEach((cpu, def )-> {
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

        private static void onLevelLoad(LevelEvent.Load event) {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                CellSavedData.INSTANCE = serverLevel.getDataStorage().computeIfAbsent(CellSavedData::new, CellSavedData::new, "art_universe_cell_data");
            }
        }

        private void commonSetup(final FMLCommonSetupEvent event) {
            new XMod();
            new ExpUpgrades(event);

            StorageCells.addCellHandler(CellHandler.INSTANCE);
            StorageCells.addCellGuiHandler(new ArtUniverseCellGuiHandler());
        }
        private static class ArtUniverseCellGuiHandler implements ICellGuiHandler {

            @Override
            public boolean isSpecializedFor(ItemStack cell) {
                return cell.getItem() instanceof ArtUniverseCellItem;
            }

            @Override
            public void openChestGui(Player player, IChestOrDrive chest, ICellHandler cellHandler, ItemStack cell) {
                MenuOpener.open(MEStorageMenu.TYPE, player, MenuLocators.forBlockEntity(((BlockEntity) chest)));
            }
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
            for (var type : ExpCraftingCPU.values()) {
                BuiltInModelHooks.addBuiltInModel(
                        Expandedae.makeId("block/crafting/" + type.getAffix() + "_formed"),
                        new CraftingCubeModel(new ExpCraftingUnitModelProvider(type))
                );
            }
        }
    }

    void init(IEventBus bus);
}
