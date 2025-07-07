package lu.kolja.expandedae.enums;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.ModList;

public enum Addons {
    EXT("expatternprovider"),
    MEGA("megacells"),
    APPFLUX("appflux"),
    ADV("advancedae"),
    APPMEK("appmek"),
    ARSENG("arseng"),
    APPBOT("appbot");

    public final String mod;

    Addons(String mod) {
        this.mod = mod;
    }

    public boolean isLoaded() {
        return ModList.get().isLoaded(mod);
    }

    public Component getUnavailableTooltip() {
        return Component.literal("Mod " + mod + " not installed!").withStyle(ChatFormatting.DARK_RED);
    }
}
