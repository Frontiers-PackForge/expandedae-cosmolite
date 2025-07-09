package lu.kolja.expandedae.mixin.compat.cosmic;

import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.stacks.KeyCounter;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.upgrades.UpgradeInventories;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import lu.kolja.expandedae.definition.ExpItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(value = PatternProviderLogic.class, remap = false)
public abstract class MixinPatternProviderLogicCosm implements IUpgradeableObject {

    @Unique
    private IUpgradeInventory eae_$upgrades = UpgradeInventories.empty();

    @Final
    @Shadow
    private PatternProviderLogicHost host;

    @Final
    @Shadow
    private IManagedGridNode mainNode;

    @Unique
    private void eae_$onUpgradesChanged() {
        this.host.saveChanges();
    }

    @Override
    public IUpgradeInventory getUpgrades() {
        return this.eae_$upgrades;
    }

    @Inject(
            method = "<init>(Lappeng/api/networking/IManagedGridNode;Lappeng/helpers/patternprovider/PatternProviderLogicHost;I)V",
            at = @At("TAIL")
    )
    private void eae_$initUpgrade(IManagedGridNode mainNode, PatternProviderLogicHost host, int patternInventorySize, CallbackInfo ci) {
        eae_$upgrades = UpgradeInventories.forMachine(host.getTerminalIcon().getItem(), 1, this::eae_$onUpgradesChanged);
    }

    @Inject(
            method = "writeToNBT",
            at = @At("TAIL")
    )
    private void eae_$saveUpgrade(CompoundTag tag, CallbackInfo ci) {
        this.eae_$upgrades.writeToNBT(tag, "upgrades");
    }

    @Inject(
            method = "readFromNBT",
            at = @At("TAIL")
    )
    private void eae_$loadUpgrade(CompoundTag tag, CallbackInfo ci) {
        this.eae_$upgrades.readFromNBT(tag, "upgrades");
    }

    @Inject(
            method = "addDrops",
            at = @At("TAIL")
    )
    private void eae_$dropUpgrade(List<ItemStack> drops, CallbackInfo ci) {
        for (var is : this.eae_$upgrades) {
            if (!is.isEmpty()) {
                drops.add(is);
            }
        }
    }

    @Inject(
            method = "clearContent",
            at = @At("TAIL")
    )
    private void eae_$clearUpgrade(CallbackInfo ci) {
        this.eae_$upgrades.clear();
    }

    @Inject(
            method = "pushPattern",
            at = @At("RETURN")
    )
    private void eae_$checkUpgrades(IPatternDetails patternDetails, KeyCounter[] inputHolder, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        if (this.eae_$upgrades.isInstalled(ExpItems.AUTO_COMPLETE_CARD)) {
            List<ICraftingCPU> matchedCpus = eae_$getCraftingCpus().stream()
                    .filter(cpu -> cpu.getJobStatus() != null)
                    .filter(cpu -> eae_$getCraftingCpus().stream()
                            .map(ICraftingCPU::getJobStatus)
                            .filter(Objects::nonNull)
                            .anyMatch(
                                    job -> cpu.getJobStatus().progress() == job.progress()
                                            && cpu.getJobStatus().crafting().equals(job.crafting())
                            )
                    ).toList();

            matchedCpus.forEach(x -> {
                var what = x.getJobStatus().crafting().what();
                var whatId = what.getId();
                for (var outputs : patternDetails.getOutputs()) {
                    var outputWhatId = outputs.what().getId();
                    if (whatId == outputWhatId) {
                        x.cancelJob();
                    }
                }
            });
        }
    }

    @Unique
    public List<ICraftingCPU> eae_$getCraftingCpus() {
        return mainNode.getGrid().getCraftingService().getCpus().stream().toList();
    }
}