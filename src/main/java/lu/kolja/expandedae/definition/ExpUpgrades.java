package lu.kolja.expandedae.definition;

import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import appeng.core.localization.GuiText;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ExpUpgrades {

    public ExpUpgrades(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, ExpBlocks.EXP_PATTERN_PROVIDER, 1, "group.exp_pattern_provider.name");
            Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, ExpItems.EXP_PATTERN_PROVIDER_PART, 1, "group.exp_pattern_provider.name");
            Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, AEBlocks.PATTERN_PROVIDER, 1, "group.pattern_provider.name");
            Upgrades.add(ExpItems.AUTO_COMPLETE_CARD, AEParts.PATTERN_PROVIDER, 1, "group.pattern_provider.name");
            Upgrades.add(ExpItems.ADVANCED_BLOCKING_CARD, AEBlocks.INTERFACE, 1, "group.interface.name");
            Upgrades.add(ExpItems.ADVANCED_BLOCKING_CARD, AEParts.INTERFACE, 1, "group.interface.name");
            Upgrades.add(ExpItems.STICKY_CARD, AEParts.STORAGE_BUS, 1, "group.storage_bus.name");
            //Upgrades.add(ExpItems.PATTERN_REFILLER_CARD, AE2wtlib.PATTERN_ENCODING_TERMINAL, 1, "group.pattern_encoding_terminal.name");

            String storageCellGroup = GuiText.StorageCells.getTranslationKey();

            for (var itemCell : ExpItems.getItemCells()) {
                Upgrades.add(AEItems.FUZZY_CARD, itemCell, 1, storageCellGroup);
                Upgrades.add(AEItems.INVERTER_CARD, itemCell, 1, storageCellGroup);
                Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, itemCell, 1, storageCellGroup);
                Upgrades.add(AEItems.VOID_CARD, itemCell, 1, storageCellGroup);
            }
        });
    }
}
