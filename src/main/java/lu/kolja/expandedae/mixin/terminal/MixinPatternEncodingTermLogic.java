package lu.kolja.expandedae.mixin.terminal;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.upgrades.UpgradeInventories;
import appeng.core.definitions.AEParts;
import appeng.helpers.IPatternTerminalLogicHost;
import appeng.parts.encoding.PatternEncodingLogic;
import net.minecraft.nbt.CompoundTag;

@Mixin(value = PatternEncodingLogic.class, remap = false)
public abstract class MixinPatternEncodingTermLogic implements IUpgradeableObject {

    @Unique
    private IUpgradeInventory eae_$upgrades = UpgradeInventories.empty();

    @Final
    @Shadow
    private IPatternTerminalLogicHost host;

    @Unique
    private void eae_$onUpgradesChanged() {
        this.host.markForSave();
    }

    @Override
    public IUpgradeInventory getUpgrades() {
        return this.eae_$upgrades;
    }

    @Inject(
            method = "<init>(Lappeng/helpers/IPatternTerminalLogicHost;)V",
            at = @At("TAIL"),
            remap = false
    )
    private void eae_$initUpgrade(IPatternTerminalLogicHost host, CallbackInfo ci) {
        eae_$upgrades = UpgradeInventories.forMachine(AEParts.PATTERN_ENCODING_TERMINAL.asItem(), 1, this::eae_$onUpgradesChanged);
    }
    @Inject(
            method = "writeToNBT",
            at = @At("TAIL"),
            remap = false
    )
    private void eae_$saveUpgrade(CompoundTag tag, CallbackInfo ci) {
        this.eae_$upgrades.writeToNBT(tag, "upgrades");
    }

    @Inject(
            method = "readFromNBT",
            at = @At("TAIL"),
            remap = false
    )
    private void eae_$loadUpgrade(CompoundTag tag, CallbackInfo ci) {
        this.eae_$upgrades.readFromNBT(tag, "upgrades");
    }
}
