package lu.kolja.expandedae.definition;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import de.mari_023.ae2wtlib.AE2wtlib;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static appeng.api.upgrades.Upgrades.add;
import static lu.kolja.expandedae.definition.ExpItems.AUTO_COMPLETE_CARD;
import static lu.kolja.expandedae.definition.ExpItems.PATTERN_REFILLER_CARD;

public class ExpUpgrades {

    public ExpUpgrades(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            add(AUTO_COMPLETE_CARD, ExpBlocks.EXP_PATTERN_PROVIDER, 1, "group.exp_pattern_provider.name");
            add(AUTO_COMPLETE_CARD, ExpItems.EXP_PATTERN_PROVIDER_PART, 1, "group.exp_pattern_provider.name");
            add(AUTO_COMPLETE_CARD, AEBlocks.PATTERN_PROVIDER, 1, "group.pattern_provider.name");
            add(AUTO_COMPLETE_CARD, AEParts.PATTERN_PROVIDER, 1, "group.pattern_provider.name");
            add(AEItems.SPEED_CARD, ExpBlocks.EXP_IO_PORT, 5, "group.exp_io_port.name");
            add(AEItems.REDSTONE_CARD, ExpBlocks.EXP_IO_PORT, 1, "group.exp_io_port.name");
            add(ExpItems.GREATER_ACCEL_CARD, ExpBlocks.EXP_IO_PORT, 5, "group.exp_io_port.name");
            /*
            Upgrades.add(SMART_BLOCKING_CARD, ExpBlocks.EXP_PATTERN_PROVIDER, 1, "group.exp_pattern_provider.name");
            Upgrades.add(SMART_BLOCKING_CARD, ExpItems.EXP_PATTERN_PROVIDER_PART, 1, "group.exp_pattern_provider.name");
            Upgrades.add(SMART_BLOCKING_CARD, AEBlocks.PATTERN_PROVIDER, 1, "group.pattern_provider.name");
            Upgrades.add(SMART_BLOCKING_CARD, AEParts.PATTERN_PROVIDER, 1, "group.pattern_provider.name");

            Upgrades.add(ADVANCED_BLOCKING_CARD, AEBlocks.INTERFACE, 1, "group.interface.name");
            Upgrades.add(ADVANCED_BLOCKING_CARD, AEParts.INTERFACE, 1, "group.interface.name");

            Upgrades.add(STICKY_CARD, AEParts.STORAGE_BUS, 1, "group.storage_bus.name");
            */
            
            add(PATTERN_REFILLER_CARD, AE2wtlib.PATTERN_ENCODING_TERMINAL, 1, "group.pattern_encoding_terminal.name");
            add(PATTERN_REFILLER_CARD, AE2wtlib.UNIVERSAL_TERMINAL, 1, "group.universal_terminal.name");
        });
    }
}
