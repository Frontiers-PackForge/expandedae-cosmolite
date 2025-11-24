package lu.kolja.expandedae.definition;

import appeng.core.AppEng;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.AEBaseMenu;
import appeng.menu.implementations.MenuTypeBuilder;
import lu.kolja.expandedae.block.entity.ExpIOPortBlockEntity;
import lu.kolja.expandedae.menu.ExpIOPortMenu;
import lu.kolja.expandedae.menu.ExpPatternProviderMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExpMenus {

    private static final Map<ResourceLocation, MenuType<?>> MENU_TYPES = new HashMap<>();

    public static Map<ResourceLocation, MenuType<?>> getMenuTypes() {
        return Collections.unmodifiableMap(MENU_TYPES);
    }

    public static final MenuType<ExpPatternProviderMenu> EXP_PATTERN_PROVIDER = create(
            "exp_pattern_provider",
            ExpPatternProviderMenu::new,
            PatternProviderLogicHost.class
    );

    public static final MenuType<ExpIOPortMenu> EXP_IO_PORT = create(
            "exp_io_port",
            ExpIOPortMenu::new,
            ExpIOPortBlockEntity.class
    );

    public static <C extends AEBaseMenu, I> MenuType<C> create(
            String id, MenuTypeBuilder.MenuFactory<C, I> factory, Class<I> host) {
        var menu = MenuTypeBuilder.create(factory, host).build(id);
        MENU_TYPES.put(AppEng.makeId(id), menu);
        return menu;
    }
}
