package lu.kolja.expandedae.xmod;

import lu.kolja.expandedae.enums.Addons;
import lu.kolja.expandedae.xmod.extendedae.ExtendedAE;
import lu.kolja.expandedae.xmod.megacells.MegaCells;

public class XMod {
    public XMod() {
        for (var mod : Addons.values()) {
            if (!mod.isLoaded()) continue;
            switch (mod) {
                case EXT -> new ExtendedAE();
                case MEGA -> new MegaCells();
            }
        }
    }
}