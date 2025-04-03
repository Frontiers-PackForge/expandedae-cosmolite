package lu.kolja.expandedae.mixin.terminal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.helpers.IPatternTerminalLogicHost;
import appeng.parts.encoding.PatternEncodingLogic;

@Mixin(value = IPatternTerminalLogicHost.class, remap = false)
public interface MixinPatternEncodingTermLogicHost extends IUpgradeableObject {
    @Shadow
    PatternEncodingLogic getLogic();

    @Override
    default IUpgradeInventory getUpgrades() {
        return ((IUpgradeableObject) this.getLogic()).getUpgrades();
    }
}
