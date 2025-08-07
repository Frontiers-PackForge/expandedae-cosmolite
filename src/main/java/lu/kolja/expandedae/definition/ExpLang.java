package lu.kolja.expandedae.definition;

import appeng.core.localization.LocalizationEnum;

public enum ExpLang implements LocalizationEnum {
    CREATIVE_TAB("creativetab.expandedae", "Expanded AE"),
    ITEM_GROUP("itemGroup.eae", "Expanded AE"),
    INFO_USELESS("info.expandedae.useless", "This is currently disabled"),

    ITEM_PATTERN_REFILLER_CARD_TOOLTIP_1("item.expandedae.pattern_refiller_card.tooltip.1", "Automatically refills the blank patterns"),
    TOOLTIP_GREATER_ACCEL_CARD_1("item.expandedae.greater_accel_card.tooltip.1", "Even greater acceleration"),
    TOOLTIP_GREATER_ACCEL_CARD_2("item.expandedae.greater_accel_card.tooltip.2", "Note: Very power hungry!"),
    ITEM_UPGRADE_TOOLTIP("item.expandedae.upgrade.tooltip", "Upgrade %s"),

    GUI_EXP_PATTERN_PROVIDER("gui.expandedae.exp_pattern_provider", "Expanded Pattern Provider"),
    GUI_EXP_IO_PORT("gui.expandedae.exp_io_port", "Expanded IO Port"),
    GUI_TOOLTIPS_MODIFY_PATTERNS("gui.tooltips.expandedae.modifyPatterns", "Modify Patterns"),
    GUI_TOOLTIPS_MODIFY_PATTERNS_HINT("gui.tooltips.expandedae.modifyPatternsHint", "Left click to multiply, right click to divide \nMultipliers: Shift 2x, Ctrl 8x"),

    GROUP_EX_PATTERN_PROVIDER_NAME("group.ex_pattern_provider.name", "ME Extended Pattern Provider"),
    GROUP_EXP_PATTERN_PROVIDER_NAME("group.exp_pattern_provider.name", "ME Expanded Pattern Provider"),
    GROUP_EXP_IO_PORT_NAME("group.exp_io_port.name", "ME Expanded IO Port"),
    GROUP_MEGA_PATTERN_PROVIDER_NAME("group.mega_pattern_provider.name", "ME MEGA Pattern Provider"),
    GROUP_PATTERN_PROVIDER_NAME("group.pattern_provider.name", "ME Pattern Provider"),
    GROUP_INTERFACE_NAME("group.interface.name", "ME Interface"),
    GROUP_STORAGE_BUS_NAME("group.storage_bus.name", "ME Storage Bus"),
    GROUP_EX_INTERFACE_NAME("group.ex_interface.name", "ME Extended Interface"),
    GROUP_OVERSIZE_INTERFACE_NAME("group.oversize_interface.name", "ME Oversize Interface"),
    GROUP_TAG_STORAGE_BUS_NAME("group.tag_storage_bus.name", "ME Tag Storage Bus"),
    GROUP_MOD_STORAGE_BUS_NAME("group.mod_storage_bus.name", "ME Mod Storage Bus"),
    GROUP_PRECISE_STORAGE_BUS_NAME("group.precise_storage_bus.name", "ME Precise Storage Bus"),
    GROUP_PATTERN_ENCODING_TERMINAL_NAME("group.pattern_encoding_terminal.name", "ME Pattern Encoding Terminal");

    private final String key;
    private final String text;

    ExpLang(String key, String text) {
        this.key = key;
        this.text = text;
    }

    @Override
    public String getEnglishText() {
        return this.text;
    }

    @Override
    public String getTranslationKey() {
        return this.key;
    }
}