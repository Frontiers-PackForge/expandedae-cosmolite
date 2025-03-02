package lu.kolja.expandedae.common.container;

import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.implementations.PatternProviderMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class ContainerExpPatternProvider extends PatternProviderMenu {

    public static final MenuType<ContainerExpPatternProvider> TYPE = MenuTypeBuilder
            .create(ContainerExpPatternProvider::new, PatternProviderLogicHost.class)
            .build("exp_pattern_provider");

    protected ContainerExpPatternProvider(int id, Inventory playerInventory, PatternProviderLogicHost host) {
        super(id, playerInventory, host);
    }
}
