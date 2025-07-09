package lu.kolja.expandedae.client;

import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.render.crafting.CraftingCubeModel;
import appeng.hooks.BuiltInModelHooks;
import appeng.init.client.InitScreens;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.client.render.ExpCraftingUnitModelProvider;
import lu.kolja.expandedae.definition.ExpCreativeTab;
import lu.kolja.expandedae.definition.ExpMenus;
import lu.kolja.expandedae.enums.ExpTiers;
import lu.kolja.expandedae.menu.ExpPatternProviderMenu;
import lu.kolja.expandedae.screen.ExpIOPortScreen;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

@OnlyIn(Dist.CLIENT)
public class ExpandedaeClient {
    public static final ExpandedaeClient INSTANCE = new ExpandedaeClient();

    @SubscribeEvent
    private void registerEvent(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.CREATIVE_MODE_TAB)) {
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ExpCreativeTab.ID, ExpCreativeTab.TAB);
        }
    }

    @SubscribeEvent
    private void clientSetup(FMLClientSetupEvent event) {
        initCraftingUnitModels();
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
