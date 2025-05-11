package lu.kolja.expandedae.mixin.misc;

import appeng.api.config.CondenserOutput;
import appeng.api.config.Setting;
import appeng.api.config.Settings;
import appeng.client.gui.Icon;
import appeng.client.gui.widgets.SettingToggleButton;
import appeng.core.localization.ButtonToolTips;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static lu.kolja.expandedae.definition.ExpSettings.BLOCKING_MODE;
import static lu.kolja.expandedae.enums.BlockingMode.*;

@Mixin(value = SettingToggleButton.class, remap = false)
public class MixinSettingToggleButton {

    @Shadow(remap = false)
    private static <T extends Enum<T>> void registerApp(Icon icon, Setting<T> setting, T val, ButtonToolTips title,
                                                        ButtonToolTips hint) {}

    @Shadow(remap = false)
    private static <T extends Enum<T>> void registerApp(Icon icon, Setting<T> setting, T val, ButtonToolTips title,
                                                        Component... tooltipLines) {}

    @Redirect(method = "<init>(Lappeng/api/config/Setting;Ljava/lang/Enum;Ljava/util/function/Predicate;Lappeng/client/gui/widgets/SettingToggleButton$IHandler;)V",
            at = @At(value = "INVOKE",
                    target = "Lappeng/client/gui/widgets/SettingToggleButton;registerApp(Lappeng/client/gui/Icon;Lappeng/api/config/Setting;Ljava/lang/Enum;Lappeng/core/localization/ButtonToolTips;Lappeng/core/localization/ButtonToolTips;)V",
                    ordinal = 0),
            remap = false)
    private <T extends Enum<T>> void register(Icon icon, Setting<T> setting, T val, ButtonToolTips title,
                                              ButtonToolTips hint) {

        registerApp(Icon.CONDENSER_OUTPUT_TRASH, Settings.CONDENSER_OUTPUT, CondenserOutput.TRASH,
                ButtonToolTips.CondenserOutput,
                ButtonToolTips.Trash);

        registerApp(Icon.CLEAR, BLOCKING_MODE, ALL,
                ButtonToolTips.InterfaceBlockingMode,
                Component.translatable("gui.expandedae.blocking_mode.all")
        );

        registerApp(Icon.BLOCKING_MODE_YES, BLOCKING_MODE, DEFAULT,
                ButtonToolTips.InterfaceBlockingMode,
                Component.translatable("gui.expandedae.blocking_mode.default")
        );

        registerApp(Icon.BLOCKING_MODE_NO, BLOCKING_MODE, SMART,
                ButtonToolTips.InterfaceBlockingMode,
                Component.translatable("gui.expandedae.blocking_mode.smart")
        );
    }
}
