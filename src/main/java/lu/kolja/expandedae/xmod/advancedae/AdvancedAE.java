package lu.kolja.expandedae.xmod.advancedae;

import lu.kolja.expandedae.definition.ExpItems;
import appeng.api.upgrades.Upgrades;
import net.pedroksl.advanced_ae.common.definitions.AAEBlocks;
import net.pedroksl.advanced_ae.common.definitions.AAEItems;

public class AdvancedAE {
    public AdvancedAE() {
        Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, AAEItems.ADV_PATTERN_PROVIDER, 1, "group.adv_pattern_provider.name");
        Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, AAEBlocks.ADV_PATTERN_PROVIDER, 1, "group.adv_pattern_provider.name");
        Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, AAEItems.SMALL_ADV_PATTERN_PROVIDER, 1, "group.adv_pattern_provider.name");
        Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, AAEBlocks.SMALL_ADV_PATTERN_PROVIDER, 1, "group.adv_pattern_provider.name");
    }
}
