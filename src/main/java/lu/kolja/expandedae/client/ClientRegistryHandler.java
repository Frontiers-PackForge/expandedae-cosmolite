package lu.kolja.expandedae.client;

import appeng.init.client.InitScreens;
import lu.kolja.expandedae.client.gui.GuiExpPatternProvider;
import lu.kolja.expandedae.common.container.ContainerExpPatternProvider;

public class ClientRegistryHandler {
    public static final ClientRegistryHandler INSTANCE = new ClientRegistryHandler();

    public void init() {
        registerGui();
    }
    public void registerGui() {
        InitScreens.register(ContainerExpPatternProvider.TYPE, GuiExpPatternProvider::new, "/screens/exp_pattern_provider.json");
    }
}
