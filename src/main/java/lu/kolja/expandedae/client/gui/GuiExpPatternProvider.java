package lu.kolja.expandedae.client.gui;

import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.ScreenStyle;
import lu.kolja.expandedae.common.container.ContainerExpPatternProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiExpPatternProvider extends PatternProviderScreen<ContainerExpPatternProvider> {
    public GuiExpPatternProvider(ContainerExpPatternProvider menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }
}
