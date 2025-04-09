package lu.kolja.expandedae.helper;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.menu.ToolboxMenu;
import net.minecraft.world.level.ItemLike;

public interface IUpgradableMenu {

    ToolboxMenu getToolbox();

    IUpgradeInventory getUpgrades();

    boolean hasUpgrade(ItemLike upgradeCard);

}