package lu.kolja.expandedae.helper.cpu;

import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.client.gui.widgets.SettingToggleButton;
import org.spongepowered.asm.mixin.Unique;

public interface ISearchScreen {
    void eae$clearSearch();

    SortOrder eae$getSortBy();

    SortDir eae$getSortDir();

    @Unique
    static <SE extends Enum<SE>> void eae$toggleButton(SettingToggleButton<SE> btn, boolean backwards) {
        SE next = btn.getNextValue(backwards);
        btn.set(next);
    }
}
