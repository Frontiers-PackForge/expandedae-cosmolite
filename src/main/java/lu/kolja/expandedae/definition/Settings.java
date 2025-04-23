package lu.kolja.expandedae.definition;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import lu.kolja.expandedae.enums.ShowInterfaces;
import com.google.common.base.Preconditions;
import appeng.api.config.Setting;

public class Settings {
    private static final Map<String, Setting<?>> SETTINGS = new HashMap<>();

    private Settings() {
    }

    private synchronized static <T extends Enum<T>> Setting<T> register(String name, Class<T> enumClass) {
        Preconditions.checkState(!SETTINGS.containsKey(name));
        var setting = new Setting<>(name, enumClass);
        SETTINGS.put(name, setting);
        return setting;
    }

    @SafeVarargs
    private synchronized static <T extends Enum<T>> Setting<T> register(String name, T firstOption, T... moreOptions) {
        Preconditions.checkState(!SETTINGS.containsKey(name));
        var setting = new Setting<T>(name, firstOption.getDeclaringClass(), EnumSet.of(firstOption, moreOptions));
        SETTINGS.put(name, setting);
        return setting;
    }

    public static final Setting<ShowInterfaces> TERMINAL_SHOW_INTERFACES= register(
            "show_interfaces", ShowInterfaces.class
    );

    public static Setting<?> getOrThrow(String name) {
        var setting = SETTINGS.get(name);
        if (setting == null) {
            throw new IllegalArgumentException("Unknown setting '" + name + "'");
        }
        return setting;
    }
}
