package lu.kolja.expandedae.menu;

import appeng.menu.implementations.IOPortMenu;
import lu.kolja.expandedae.block.entity.ExpIOPortBlockEntity;
import net.minecraft.world.entity.player.Inventory;

public class ExpIOPortMenu extends IOPortMenu {
    public ExpIOPortMenu(int id, Inventory ip, ExpIOPortBlockEntity host) {
        super(id, ip, host);
    }
}
