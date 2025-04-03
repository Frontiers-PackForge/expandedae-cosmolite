package lu.kolja.expandedae;

import appeng.core.AppEng;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;

public class Platform {
    public static void registerMenuType(String id, MenuType<?> menuType) {
        ForgeRegistries.MENU_TYPES.register(AppEng.makeId(id), menuType);
    }
}
