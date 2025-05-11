package lu.kolja.expandedae.menu;

import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.implementations.PatternProviderMenu;
import lu.kolja.expandedae.definition.ExpMenus;
import net.minecraft.world.entity.player.Inventory;

public class ExpPatternProviderMenu extends PatternProviderMenu {
    public ExpPatternProviderMenu(int id, Inventory playerInventory, PatternProviderLogicHost host) {
        super(ExpMenus.EXP_PATTERN_PROVIDER, id, playerInventory, host);
    }
}
