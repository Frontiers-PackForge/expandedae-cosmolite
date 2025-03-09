package lu.kolja.expandedae.mixin;

import java.util.List;
import java.util.Objects;

import lu.kolja.expandedae.definition.ExpItems;
import com.glodblock.github.appflux.common.AFItemAndBlock;
import com.glodblock.github.appflux.common.me.energy.EnergyHandler;
import com.glodblock.github.appflux.common.me.service.IEnergyDistributor;
import com.glodblock.github.appflux.util.AFUtil;
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
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageService;
import appeng.api.stacks.KeyCounter;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.upgrades.UpgradeInventories;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

@Mixin(value = PatternProviderLogic.class, remap = false)
public class AppFluxMixinPatternProviderLogic implements IUpgradeableObject, IEnergyDistributor {

    @Unique
    private IUpgradeInventory eae_af_$upgrades = UpgradeInventories.empty();
    @Final
    @Shadow
    private PatternProviderLogicHost host;

    @Final
    @Shadow
    private IManagedGridNode mainNode;

    @Final
    @Shadow
    private IActionSource actionSource;

    @Unique
    private void af_$onUpgradesChanged() {
        this.host.saveChanges();
    }

    @Override
    public IUpgradeInventory getUpgrades() {
        return this.eae_af_$upgrades;
    }

    @Inject(
            method = "<init>(Lappeng/api/networking/IManagedGridNode;Lappeng/helpers/patternprovider/PatternProviderLogicHost;I)V",
            at = @At("TAIL")
    )
    private void initUpgrade(IManagedGridNode mainNode, PatternProviderLogicHost host, int patternInventorySize, CallbackInfo ci) {
        eae_af_$upgrades = UpgradeInventories.forMachine(host.getTerminalIcon().getItem(), 2, this::af_$onUpgradesChanged);
        this.mainNode.addService(IEnergyDistributor.class, this);
    }

    @Inject(
            method = "writeToNBT",
            at = @At("TAIL")
    )
    private void saveUpgrade(CompoundTag tag, CallbackInfo ci) {
        this.eae_af_$upgrades.writeToNBT(tag, "upgrades");
    }

    @Inject(
            method = "readFromNBT",
            at = @At("TAIL")
    )
    private void loadUpgrade(CompoundTag tag, CallbackInfo ci) {
        this.eae_af_$upgrades.readFromNBT(tag, "upgrades");
    }

    @Inject(
            method = "addDrops",
            at = @At("TAIL")
    )
    private void dropUpgrade(List<ItemStack> drops, CallbackInfo ci) {
        for (var is : this.eae_af_$upgrades) {
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
        this.eae_af_$upgrades.clear();
    }

    @Override
    public void distribute() {
        if (this.eae_af_$upgrades.isInstalled(AFItemAndBlock.INDUCTION_CARD)) {
            var storage = this.af_getStorage();
            var gird = this.mainNode.getGrid();
            var self = this.host.getBlockEntity();
            if (storage != null && self.getLevel() != null) {
                for (var d : AFUtil.getSides(this.host)) {
                    var te = self.getLevel().getBlockEntity(self.getBlockPos().offset(d.getNormal()));
                    var thatGrid = AFUtil.getGrid(te, d.getOpposite());
                    if (te != null && thatGrid != gird && !AFUtil.isBlackListTE(te, d.getOpposite())) {
                        EnergyHandler.send(te, d.getOpposite(), storage, this.actionSource);
                    }
                }
            }
        }
    }

    @Unique
    private IStorageService af_getStorage() {
        if (this.mainNode.getGrid() != null) {
            return this.mainNode.getGrid().getStorageService();
        }
        return null;
    }

    @Inject(
            method = "pushPattern",
            at = @At("RETURN")
    )
    private void checkUpgrades(IPatternDetails patternDetails, KeyCounter[] inputHolder, CallbackInfoReturnable<Boolean> cir) {
        if (this.eae_af_$upgrades.isInstalled(ExpItems.AUTO_COMPLETE_CARD)) {
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
                        x.cancelJob();
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
