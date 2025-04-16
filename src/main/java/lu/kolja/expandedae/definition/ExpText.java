package lu.kolja.expandedae.definition;

import appeng.core.localization.LocalizationEnum;
import net.minecraft.ChatFormatting;

public enum ExpText implements LocalizationEnum {
    modifyPatterns("Modify Patterns", Type.TOOLTIP),
    modifyPatternsHint("Modify Patterns in this pattern provider.", Type.TOOLTIP);

    private final String englishText;
    private final ExpText.Type type;
    public static final ChatFormatting TOOLTIP_DEFAULT_COLOR = ChatFormatting.GRAY;

    ExpText(String englishText, ExpText.Type type) {
        this.englishText = englishText;
        this.type = type;
    }

    public String getEnglishText() {
        return this.englishText;
    }

    public String getTranslationKey() {
        return String.format("%s.%s.%s", this.type.root, "expandedae", this.name());
    }

    private enum Type {
        GUI("gui"),
        TOOLTIP("gui.tooltips"),
        EMI_CATEGORY("emi.category"),
        EMI_TEXT("emi.text");

        private final String root;

        Type(String root) {
            this.root = root;
        }
    }
}
