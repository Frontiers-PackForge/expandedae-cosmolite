package lu.kolja.expandedae.xmod;

import lu.kolja.expandedae.xmod.advancedae.AdvancedAE;
import lu.kolja.expandedae.xmod.appflux.AppFlux;
import lu.kolja.expandedae.xmod.extendedae.ExtendedAE;
import lu.kolja.expandedae.xmod.megacells.MegaCells;
import net.minecraftforge.fml.ModList;

public class XMod {
    private final String[] mods = {"appflux", "expatternprovider", "megacells", "advanced_ae"};

    public XMod() {
        for (var mod : mods) {
            if (!ModList.get().isLoaded(mod)) continue;
            switch (mod) {
                case "appflux" -> new AppFlux();
                case "expatternprovider" -> new ExtendedAE();
                case "megacells" -> new MegaCells();
                case "advanced_ae" -> new AdvancedAE();
            }
        }
    }
}
