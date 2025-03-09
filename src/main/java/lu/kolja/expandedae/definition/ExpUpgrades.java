package lu.kolja.expandedae.definition;

import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEParts;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ExpUpgrades {

    public ExpUpgrades(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, ExpBlocks.EXP_PATTERN_PROVIDER, 1, "group.exp_pattern_provider.name");
            Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, ExpItems.EXP_PATTERN_PROVIDER_PART, 1, "group.exp_pattern_provider.name");
            Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, AEBlocks.PATTERN_PROVIDER, 1, "group.pattern_provider.name");
            Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, AEParts.PATTERN_PROVIDER, 1, "group.pattern_provider.name");
        });
    }
}
