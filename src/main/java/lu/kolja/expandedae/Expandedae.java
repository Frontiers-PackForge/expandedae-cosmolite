package lu.kolja.expandedae;

import com.mojang.logging.LogUtils;
import lu.kolja.expandedae.client.ExpandedaeClient;
import lu.kolja.expandedae.datagen.conditionals.ModNotLoadedCondition;
import lu.kolja.expandedae.definition.ExpBlockEntities;
import lu.kolja.expandedae.definition.ExpBlocks;
import lu.kolja.expandedae.definition.ExpItems;
import lu.kolja.expandedae.definition.ExpMenus;
import lu.kolja.expandedae.definition.ExpUpgrades;
import lu.kolja.expandedae.xmod.XMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(Expandedae.MODID)
public class Expandedae {

    public static final String MODID = "expandedae";
    public static final Logger LOGGER = LogUtils.getLogger();

    //getActionableNode().getGrid().getStorageService().getInventory().insert() TODO IMPLEMENT TO STICKY CARD

    public Expandedae(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
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
            if (event.getRegistryKey().equals(Registries.MENU)) {
                ExpMenus.getMenuTypes().forEach(ForgeRegistries.MENU_TYPES::register);
            }
        });
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.register(ExpandedaeClient.INSTANCE);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        new XMod();
        new ExpUpgrades(event);
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation makeId(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}