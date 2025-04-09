package lu.kolja.expandedae;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import appeng.core.AppEng;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.ForgeRegistries;

public class Platform {
    Logger log = LoggerFactory.getLogger(Platform.class);

    public static void registerMenuType(String id, MenuType<?> menuType) {
        ForgeRegistries.MENU_TYPES.register(AppEng.makeId(id), menuType);
    }

}
