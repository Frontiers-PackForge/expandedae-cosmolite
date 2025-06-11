package lu.kolja.expandedae.xmod;

import lu.kolja.expandedae.xmod.advancedae.AdvancedAE;
import lu.kolja.expandedae.xmod.appflux.AppFlux;
import lu.kolja.expandedae.xmod.extendedae.ExtendedAE;
import lu.kolja.expandedae.xmod.megacells.MegaCells;
import net.minecraftforge.fml.ModList;

public class XMod {
    private enum Mods {
        EXT("expatternprovider"),
        MEGA("megacells"),
        APPFLUX("appflux"),
        ADV("advancedae");

        private final String mod;
        Mods(String mod) { this.mod = mod; }
        boolean isLoaded() { return ModList.get().isLoaded(mod); }
    }


    public XMod() {
        for (var mod : Mods.values()) {
            if (!mod.isLoaded()) continue;
            switch (mod) {
                case EXT -> new ExtendedAE();
                case MEGA -> new MegaCells();
                case APPFLUX -> new AppFlux();
                case ADV -> new AdvancedAE();
            }
        }
    }
}