package lu.kolja.expandedae.xmod.appflux;

import lu.kolja.expandedae.definition.ExpBlocks;
import lu.kolja.expandedae.definition.ExpItems;
import com.glodblock.github.appflux.common.AFItemAndBlock;
import appeng.api.upgrades.Upgrades;

import net.minecraftforge.fml.ModList;

import gripe._90.megacells.definition.MEGABlocks;
import gripe._90.megacells.definition.MEGAItems;

public class AppFlux {
    public AppFlux() {
        Upgrades.add(AFItemAndBlock.INDUCTION_CARD, ExpBlocks.EXP_PATTERN_PROVIDER, 1, "group.exp_pattern_provider.name");
        Upgrades.add(AFItemAndBlock.INDUCTION_CARD, ExpItems.EXP_PATTERN_PROVIDER_PART, 1, "group.exp_pattern_provider.name");

        if (ModList.get().isLoaded("megacells")) {
            Upgrades.add(AFItemAndBlock.INDUCTION_CARD, MEGABlocks.MEGA_PATTERN_PROVIDER.asItem(), 1, "group.mega_pattern_provider.name");
            Upgrades.add(AFItemAndBlock.INDUCTION_CARD, MEGAItems.MEGA_PATTERN_PROVIDER.asItem(), 1, "group.mega_pattern_provider.name");
        }
    }
}
