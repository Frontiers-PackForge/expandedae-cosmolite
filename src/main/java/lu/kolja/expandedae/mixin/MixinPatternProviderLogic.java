package lu.kolja.expandedae.mixin;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lu.kolja.expandedae.definition.ExpItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.stacks.KeyCounter;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.upgrades.UpgradeInventories;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

@Mixin(value = PatternProviderLogic.class, remap = false)
public abstract class MixinPatternProviderLogic implements IUpgradeableObject {

    @Unique
    private IUpgradeInventory eae_$upgrades = UpgradeInventories.empty();
    @Final
    @Shadow
    private PatternProviderLogicHost host;

    @Final
    @Shadow
    private IManagedGridNode mainNode;

    @Shadow protected abstract void onPushPatternSuccess(IPatternDetails pattern);

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
    private void initUpgrade(IManagedGridNode mainNode, PatternProviderLogicHost host, int patternInventorySize, CallbackInfo ci) {
        eae_$upgrades = UpgradeInventories.forMachine(host.getTerminalIcon().getItem(), 1, this::eae_$onUpgradesChanged);
    }

    @Inject(
            method = "writeToNBT",
            at = @At("TAIL")
    )
    private void saveUpgrade(CompoundTag tag, CallbackInfo ci) {
        this.eae_$upgrades.writeToNBT(tag, "upgrades");
    }

    @Inject(
            method = "readFromNBT",
            at = @At("TAIL")
    )
    private void loadUpgrade(CompoundTag tag, CallbackInfo ci) {
        this.eae_$upgrades.readFromNBT(tag, "upgrades");
    }

    @Inject(
            method = "addDrops",
            at = @At("TAIL")
    )
    private void dropUpgrade(List<ItemStack> drops, CallbackInfo ci) {
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
    private void clearUpgrade(CallbackInfo ci) {
        this.eae_$upgrades.clear();
    }

    @Inject(
            method = "pushPattern",
            at = @At("RETURN")
    )
    private void checkUpgrades(IPatternDetails patternDetails, KeyCounter[] inputHolder, CallbackInfoReturnable<Boolean> cir) {
        if (this.eae_$upgrades.isInstalled(ExpItems.AUTO_COMPLETE_CARD)) {
            List<ICraftingCPU> matchedCpus = expandedae$getCraftingCpus().stream()
                    .filter(cpu -> cpu.getJobStatus() != null)
                    .filter(cpu -> expandedae$getCraftingCpus().stream()
                            .map(ICraftingCPU::getJobStatus)
                            .filter(Objects::nonNull)
                            .anyMatch(
                                    job -> cpu.getJobStatus().progress() == job.progress()
                                            && cpu.getJobStatus().crafting().equals(job.crafting())
                            )
                    ).toList();

            matchedCpus.forEach(x -> {
                var whatId = x.getJobStatus().crafting().what().getId();
                //var amount = x.getJobStatus().crafting().amount(); DEBUG PURPOSES
                for (var outputs : patternDetails.getOutputs()) {
                    var outputWhatId = outputs.what().getId();
                    //var outputAmount = outputs.amount(); DEBUG PURPOSES
                    if (whatId == outputWhatId) {
                        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
                        service.schedule(x::cancelJob, 10L, TimeUnit.MILLISECONDS);
                        return;
                    }
                }
            });
        }
    }

    @Unique
    public List<ICraftingCPU> expandedae$getCraftingCpus() {
        return mainNode.getGrid().getCraftingService().getCpus().stream().toList();
    }
}