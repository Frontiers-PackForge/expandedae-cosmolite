package lu.kolja.expandedae.definition;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import de.mari_023.ae2wtlib.AE2wtlib;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static appeng.api.upgrades.Upgrades.add;
import static lu.kolja.expandedae.definition.ExpItems.PATTERN_REFILLER_CARD;

public class ExpUpgrades {

    public ExpUpgrades(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            add(AEItems.AUTO_COMPLETE_CARD, ExpBlocks.EXP_PATTERN_PROVIDER, 1, "group.exp_pattern_provider.name");
            add(AEItems.AUTO_COMPLETE_CARD, ExpItems.EXP_PATTERN_PROVIDER_PART, 1, "group.exp_pattern_provider.name");
            add(AEItems.AUTO_COMPLETE_CARD, AEBlocks.PATTERN_PROVIDER, 1, "group.pattern_provider.name");
            add(AEItems.AUTO_COMPLETE_CARD, AEParts.PATTERN_PROVIDER, 1, "group.pattern_provider.name");

            add(AEItems.SPEED_CARD, ExpBlocks.EXP_IO_PORT, 5, "group.exp_io_port.name");
            add(AEItems.REDSTONE_CARD, ExpBlocks.EXP_IO_PORT, 1, "group.exp_io_port.name");
            add(ExpItems.GREATER_ACCEL_CARD, ExpBlocks.EXP_IO_PORT, 5, "group.exp_io_port.name");
            
            add(PATTERN_REFILLER_CARD, AE2wtlib.PATTERN_ENCODING_TERMINAL, 1, "group.pattern_encoding_terminal.name");
            add(PATTERN_REFILLER_CARD, AE2wtlib.UNIVERSAL_TERMINAL, 1, "group.universal_terminal.name");
        });
    }
}
