package lu.kolja.expandedae.mixin.compat.appflux;

import appeng.api.config.Actionable;
import appeng.api.config.LockCraftingMode;
import appeng.api.crafting.IPatternDetails;
import appeng.api.implementations.blockentities.ICraftingMachine;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.client.gui.me.common.FinishedJobToast;
import appeng.client.gui.me.common.MEStorageScreen;
import appeng.core.AEConfig;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.items.tools.powered.WirelessTerminalItem;
import appeng.util.ConfigManager;
import appeng.util.SearchInventoryEvent;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import lu.kolja.expandedae.definition.ExpItems;
import lu.kolja.expandedae.definition.ExpSettings;
import lu.kolja.expandedae.enums.BlockingMode;
import lu.kolja.expandedae.helper.patternprovider.IPatternProviderLogic;
import lu.kolja.expandedae.helper.patternprovider.PatternProviderTarget;
import lu.kolja.expandedae.helper.patternprovider.PatternProviderTargetCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mixin(value = PatternProviderLogic.class, remap = false)
public abstract class MixinPatternProviderLogicAppFlux implements IUpgradeableObject, IPatternProviderLogic {

    @Unique
    private PatternProviderTargetCache[] expandedae$targetCaches;
    @Final
    @Shadow
    private PatternProviderLogicHost host;
    @Final
    @Shadow
    private IManagedGridNode mainNode;
    @Final
    @Shadow
    private IActionSource actionSource;

    @Shadow(remap = false)
    @Final
    private ConfigManager configManager;

    @Shadow @Final private Set<AEKey> patternInputs;

    @Shadow private int roundRobinIndex;

    @Shadow @Final private List<GenericStack> sendList;

    @Shadow @Final private List<IPatternDetails> patterns;

    @Shadow private Direction sendDirection;

    @Shadow public abstract LockCraftingMode getCraftingLockedReason();

    @Shadow protected abstract Set<Direction> getActiveSides();

    @Shadow protected abstract void onPushPatternSuccess(IPatternDetails pattern);

    @Shadow protected abstract <T> void rearrangeRoundRobin(List<T> list);

    @Shadow public abstract boolean isBlocking();

    @Shadow protected abstract boolean sendStacksOut();

    @Shadow protected abstract void addToSendList(AEKey what, long amount);

    @Inject(
            method = "<init>(Lappeng/api/networking/IManagedGridNode;Lappeng/helpers/patternprovider/PatternProviderLogicHost;I)V",
            at = @At("TAIL")
    )
    private void eae_$initUpgrade(IManagedGridNode mainNode, PatternProviderLogicHost host, int patternInventorySize, CallbackInfo ci) {
        this.expandedae$targetCaches = new PatternProviderTargetCache[6];
    }

    @Inject(
            method = "pushPattern",
            at = @At("RETURN")
    )
    private void eae_$checkUpgrades(IPatternDetails patternDetails, KeyCounter[] inputHolder, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        if (this.getUpgrades().isInstalled(ExpItems.AUTO_COMPLETE_CARD)) {
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
                        var minecraft = Minecraft.getInstance();
                        if (AEConfig.instance().isNotifyForFinishedCraftingJobs()
                                && !(minecraft.screen instanceof MEStorageScreen<?>)
                                && minecraft.player != null && expandedae$hasNotificationEnablingItem(minecraft.player)) {
                            minecraft.getToasts().addToast(new FinishedJobToast(what, 1)); //set to 1 since the card doesn't support more
                        }
                        return;
                    }
                }
            });
        }
    }
    @Unique
    private static boolean expandedae$hasNotificationEnablingItem(LocalPlayer player) {
        for (ItemStack stack : SearchInventoryEvent.getItems(player)) {
            if (!stack.isEmpty()
                    && stack.getItem() instanceof WirelessTerminalItem wirelessTerminal
                    // Should have some power
                    && wirelessTerminal.getAECurrentPower(stack) > 0
                    // Should be linked (we don't know if it's linked to the grid for which we get notifications)
                    && wirelessTerminal.getLinkedPosition(stack) != null) {
                return true;
            }
        }
        return false;
    }

    @Unique
    public List<ICraftingCPU> eae_$getCraftingCpus() {
        return mainNode.getGrid().getCraftingService().getCpus().stream().toList();
    }

    @Inject(method = "<init>(Lappeng/api/networking/IManagedGridNode;Lappeng/helpers/patternprovider/PatternProviderLogicHost;I)V",
            at = @At("TAIL"),
            remap = false)
    private void PatternProviderLogic(IManagedGridNode mainNode, PatternProviderLogicHost host,
                                      int patternInventorySize, CallbackInfo ci) {
        configManager.registerSetting(ExpSettings.BLOCKING_MODE, BlockingMode.DEFAULT);
    }

    @Override
    public BlockingMode expandedae$getBlockingMode() {
        return configManager.getSetting(ExpSettings.BLOCKING_MODE);
    }

    /**
     * @author Kolja
     * @reason .
     */
    @Overwrite
    public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
        if (this.sendList.isEmpty() && this.mainNode.isActive() && this.patterns.contains(patternDetails)) {
            BlockEntity be = this.host.getBlockEntity();
            Level level = be.getLevel();
            if (this.getCraftingLockedReason() == LockCraftingMode.NONE) {
                record PushTarget(Direction direction, PatternProviderTarget target) {}

                var possibleTargets = new ArrayList<PushTarget>();

                for (Direction direction : this.getActiveSides()) {
                    BlockPos adjPos = be.getBlockPos().relative(direction);
                    BlockEntity adjBe = level.getBlockEntity(adjPos);
                    Direction adjBeSide = direction.getOpposite();
                    ICraftingMachine craftingMachine = ICraftingMachine.of(level, adjPos, adjBeSide, adjBe);
                    if (craftingMachine != null && craftingMachine.acceptsPlans()) {
                        if (craftingMachine.pushPattern(patternDetails, inputHolder, adjBeSide)) {
                            this.onPushPatternSuccess(patternDetails);
                            return true;
                        }
                    } else {
                        PatternProviderTarget adapter = this.expandedae$findAdapter(direction);
                        if (adapter != null) {
                            possibleTargets.add(new PushTarget(direction, adapter));
                        }
                    }
                }

                if (patternDetails.supportsPushInputsToExternalInventory()) {
                    this.rearrangeRoundRobin(possibleTargets);

                    for (PushTarget target : possibleTargets) {
                        Direction direction = target.direction();
                        PatternProviderTarget adapter = target.target();
                        switch (expandedae$getBlockingMode()) {
                            case ALL -> {

                                if ((!this.isBlocking() || adapter.getStorage().getAvailableStacks().isEmpty()) && this.expandedae$adapterAcceptsAll(adapter, inputHolder)) {
                                    patternDetails.pushInputsToExternalInventory(inputHolder, (what, amount) -> {
                                        long inserted = adapter.insert(what, amount, Actionable.MODULATE);
                                        if (inserted < amount) {
                                            this.addToSendList(what, amount - inserted);
                                        }
                                    });
                                    this.onPushPatternSuccess(patternDetails);
                                    this.sendDirection = direction;
                                    this.sendStacksOut();
                                    ++this.roundRobinIndex;
                                    return true;
                                }
                            }
                            case SMART -> {
                                if ((!this.isBlocking() || adapter.getStorage().getAvailableStacks().isEmpty() || adapter.onlyHasPatternInput(this.patternInputs)) && this.expandedae$adapterAcceptsAll(adapter, inputHolder)) {
                                    patternDetails.pushInputsToExternalInventory(inputHolder, (what, amount) -> {
                                        long inserted = adapter.insert(what, amount, Actionable.MODULATE);
                                        if (inserted < amount) {
                                            this.addToSendList(what, amount - inserted);
                                        }
                                    });
                                    this.onPushPatternSuccess(patternDetails);
                                    this.sendDirection = direction;
                                    this.sendStacksOut();
                                    ++this.roundRobinIndex;
                                    return true;
                                }
                            }
                            case DEFAULT -> {
                                if ((!this.isBlocking() || !adapter.containsPatternInput(this.patternInputs)) && this.expandedae$adapterAcceptsAll(adapter, inputHolder)) {
                                    patternDetails.pushInputsToExternalInventory(inputHolder, (what, amount) -> {
                                        long inserted = adapter.insert(what, amount, Actionable.MODULATE);
                                        if (inserted < amount) {
                                            this.addToSendList(what, amount - inserted);
                                        }
                                    });
                                    this.onPushPatternSuccess(patternDetails);
                                    this.sendDirection = direction;
                                    this.sendStacksOut();
                                    ++this.roundRobinIndex;
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Unique
    private @Nullable PatternProviderTarget expandedae$findAdapter(Direction side) {
        if (this.expandedae$targetCaches[side.get3DDataValue()] == null) {
            BlockEntity thisBe = this.host.getBlockEntity();
            this.expandedae$targetCaches[side.get3DDataValue()] = new PatternProviderTargetCache((ServerLevel) thisBe.getLevel(), thisBe.getBlockPos().relative(side), side.getOpposite(), this.actionSource);
        }
        return this.expandedae$targetCaches[side.get3DDataValue()].find();
    }

    @Unique
    private boolean expandedae$adapterAcceptsAll(PatternProviderTarget target, KeyCounter[] inputHolder) {
        for (KeyCounter entries : inputHolder) {
            for (Object2LongMap.Entry<AEKey> input : entries) {
                long inserted = target.insert(input.getKey(), input.getLongValue(), Actionable.SIMULATE);
                if (inserted == 0L) {
                    return false;
                }
            }
        }
        return true;
    }
}
