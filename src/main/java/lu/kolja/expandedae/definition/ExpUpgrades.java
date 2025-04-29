package lu.kolja.expandedae.definition;

import org.jetbrains.annotations.Nullable;
import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEParts;
import appeng.core.localization.GuiText;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import de.mari_023.ae2wtlib.AE2wtlib;
import static lu.kolja.expandedae.definition.ExpItems.*;
import static lu.kolja.expandedae.definition.ExpBlocks.*;
import static appeng.core.definitions.AEBlocks.*;
import static appeng.core.definitions.AEParts.*;

public class ExpUpgrades {

    public ExpUpgrades(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            add(AUTO_COMPLETE_CARD, EXP_PATTERN_PROVIDER, 1, "group.exp_pattern_provider.name");
            add(AUTO_COMPLETE_CARD, EXP_PATTERN_PROVIDER_PART, 1, "group.exp_pattern_provider.name");
            add(AUTO_COMPLETE_CARD, AEBlocks.PATTERN_PROVIDER, 1, "group.pattern_provider.name");
            add(AUTO_COMPLETE_CARD, AEParts.PATTERN_PROVIDER, 1, "group.pattern_provider.name");
            add(ADVANCED_BLOCKING_CARD, AEBlocks.INTERFACE, 1, "group.interface.name");
            add(ADVANCED_BLOCKING_CARD, AEParts.INTERFACE, 1, "group.interface.name");
            add(STICKY_CARD, STORAGE_BUS, 1, "group.storage_bus.name");
            add(PATTERN_REFILLER_CARD, AE2wtlib.PATTERN_ENCODING_TERMINAL, 1, "group.pattern_encoding_terminal.name");
            add(PATTERN_REFILLER_CARD, AE2wtlib.UNIVERSAL_TERMINAL, 1, "group.universal_terminal.name");

            add(GREATER_ACCEL_CARD, IO_PORT, 3, GuiText.IOPort.getTranslationKey());
            add(GREATER_ACCEL_CARD, MOLECULAR_ASSEMBLER, 5, GuiText.MolecularAssembler.getTranslationKey());
            add(GREATER_ACCEL_CARD, INSCRIBER, 4, GuiText.Inscriber.getTranslationKey());
        });
    }
    private synchronized void add(ItemLike upgradeCard, ItemLike upgradableObject, int maxSupported, @Nullable String tooltipGroup) {
        Upgrades.add(upgradeCard, upgradableObject, maxSupported, tooltipGroup);
    }
}
