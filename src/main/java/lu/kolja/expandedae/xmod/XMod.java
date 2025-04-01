package lu.kolja.expandedae.xmod;

import lu.kolja.expandedae.xmod.extendedae.ExtendedAE;
import lu.kolja.expandedae.xmod.megacells.MegaCells;
import net.minecraftforge.fml.ModList;

public class XMod {
    private final String[] mods = {"expatternprovider", "megacells"};

    public XMod() {
        for (var mod : mods) {
            if (!ModList.get().isLoaded(mod)) continue;
            switch (mod) {
                case "expatternprovider" -> new ExtendedAE();
                case "megacells" -> new MegaCells();
            }
        }
    }
}
