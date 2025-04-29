package lu.kolja.expandedae.mixin.greateraccel;

import lu.kolja.expandedae.definition.ExpItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import appeng.api.implementations.IPowerChannelState;
import appeng.api.implementations.blockentities.ICraftingMachine;
import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.stacks.AEItemKey;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.blockentity.crafting.IMolecularAssemblerSupportedPattern;
import appeng.blockentity.crafting.MolecularAssemblerBlockEntity;
import appeng.blockentity.grid.AENetworkInvBlockEntity;
import appeng.core.definitions.AEItems;
import appeng.core.localization.GuiText;
import appeng.core.localization.Tooltips;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.network.TargetPoint;
import appeng.core.sync.packets.AssemblerAnimationPacket;
import appeng.crafting.CraftingEvent;
import appeng.util.inv.AppEngInternalInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = MolecularAssemblerBlockEntity.class, remap = false)
public abstract class MixinMolecularAssemblerBlockEntity extends AENetworkInvBlockEntity
        implements IUpgradeableObject, IGridTickable, ICraftingMachine, IPowerChannelState {

    @Final @Shadow private CraftingContainer craftingInv;
    @Final
    @Shadow private AppEngInternalInventory gridInv;
    @Final
    @Shadow private AppEngInternalInventory patternInv;
    @Final
    @Shadow private InternalInventory gridInvExt;
    @Final
    @Shadow private InternalInventory internalInv;
    @Final @Shadow private IUpgradeInventory upgrades;
    @Shadow private boolean isPowered;
    @Shadow private Direction pushDirection;
    @Shadow private ItemStack myPattern;
    @Shadow private IMolecularAssemblerSupportedPattern myPlan;
    @Shadow private double progress;
    @Shadow private boolean isAwake;
    @Shadow private boolean forcePlan;
    @Shadow private boolean reboot;

    @Shadow protected abstract void pushOut(ItemStack output);

    @Shadow protected abstract void ejectHeldItems();

    @Shadow protected abstract void updateSleepiness();

    @Shadow protected abstract int userPower(int ticksPassed, int bonusValue, double acceleratorTax);

    public MixinMolecularAssemblerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
    }

    @Inject(method = "getCraftingMachineInfo", at = @At("RETURN"))
    private void getCraftingMachineInfo(CallbackInfoReturnable<PatternContainerGroup> cir) {
        var cards = getInstalledUpgrades(ExpItems.GREATER_ACCEL_CARD);
        if (cards == 0) return;
        cir.getReturnValue().tooltip().add(GuiText.CompatibleUpgrade.text(
                Tooltips.of(ExpItems.GREATER_ACCEL_CARD.asItem().getDescription()),
                Tooltips.ofUnformattedNumber(cards))
        );
    }

    /**
     * @author Kolja
     * @reason speed
     */
    @Overwrite
    public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
        if (!this.gridInv.getStackInSlot(9).isEmpty()) {
            this.pushOut(this.gridInv.getStackInSlot(9));

            // did it eject?
            if (this.gridInv.getStackInSlot(9).isEmpty()) {
                this.saveChanges();
            }

            this.ejectHeldItems();
            this.updateSleepiness();
            this.progress = 0;
            return this.isAwake ? TickRateModulation.IDLE : TickRateModulation.SLEEP;
        }

        if (this.myPlan == null) {
            this.updateSleepiness();
            return TickRateModulation.SLEEP;
        }

        if (this.reboot) {
            ticksSinceLastCall = 1;
        }

        if (!this.isAwake) {
            return TickRateModulation.SLEEP;
        }

        this.reboot = false;
        int speed = 10;
        switch (this.upgrades.getInstalledUpgrades(AEItems.SPEED_CARD)) {
            case 0 -> this.progress += this.userPower(ticksSinceLastCall, 0, 1.0);
            case 1 -> this.progress += this.userPower(ticksSinceLastCall, speed = 13, 1.3);
            case 2 -> this.progress += this.userPower(ticksSinceLastCall, speed = 17, 1.7);
            case 3 -> this.progress += this.userPower(ticksSinceLastCall, speed = 20, 2.0);
            case 4 -> this.progress += this.userPower(ticksSinceLastCall, speed = 25, 2.5);
            case 5 -> this.progress += this.userPower(ticksSinceLastCall, speed = 50, 5.0);
        }

        if (this.progress >= 100) {
            for (int x = 0; x < this.craftingInv.getContainerSize(); x++) {
                this.craftingInv.setItem(x, this.gridInv.getStackInSlot(x));
            }

            this.progress = 0;
            final ItemStack output = this.myPlan.assemble(this.craftingInv, this.getLevel());
            if (!output.isEmpty()) {
                CraftingEvent.fireAutoCraftingEvent(getLevel(), this.myPlan, output, this.craftingInv);

                // pushOut might reset the plan back to null, so get the remaining items before
                var craftingRemainders = this.myPlan.getRemainingItems(this.craftingInv);

                this.pushOut(output.copy());

                for (int x = 0; x < this.craftingInv.getContainerSize(); x++) {
                    this.gridInv.setItemDirect(x, craftingRemainders.get(x));
                }

                if (this.patternInv.isEmpty()) {
                    this.forcePlan = false;
                    this.myPlan = null;
                    this.pushDirection = null;
                }

                this.ejectHeldItems();

                var item = AEItemKey.of(output);
                if (item != null) {
                    final TargetPoint where = new TargetPoint(this.worldPosition.getX(), this.worldPosition.getY(),
                            this.worldPosition.getZ(), 32,
                            this.level);
                    NetworkHandler.instance()
                            .sendToAllAround(new AssemblerAnimationPacket(this.worldPosition, (byte) speed, item),
                                    where);
                }

                this.saveChanges();
                this.updateSleepiness();
                return this.isAwake ? TickRateModulation.IDLE : TickRateModulation.SLEEP;
            }
        }

        return TickRateModulation.FASTER;
    }
}
