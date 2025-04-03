package lu.kolja.expandedae.mixin.terminal;

import lu.kolja.expandedae.definition.ExpItems;
import lu.kolja.expandedae.menu.helper.IUpgradableMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.core.definitions.AEItems;
import appeng.helpers.IPatternTerminalMenuHost;
import appeng.menu.AEBaseMenu;
import appeng.menu.ToolboxMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.slot.RestrictedInputSlot;
import appeng.parts.encoding.PatternEncodingLogic;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.ItemLike;

@Mixin(value = PatternEncodingTermMenu.class, remap = false)
public abstract class MixinPatternEncodingTermMenu extends AEBaseMenu implements IUpgradableMenu {

    @Shadow @Final private PatternEncodingLogic encodingLogic;
    @Unique
    private ToolboxMenu eae_$toolbox;

    @Shadow @Final private RestrictedInputSlot blankPatternSlot;

    @Shadow @Final private RestrictedInputSlot encodedPatternSlot;

    public MixinPatternEncodingTermMenu(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
        super(menuType, id, playerInventory, host);
    }

    @Inject(
            method = "<init>(Lnet/minecraft/world/inventory/MenuType;ILnet/minecraft/world/entity/player/Inventory;Lappeng/helpers/IPatternTerminalMenuHost;Z)V",
            at = @At("TAIL"),
            remap = false
    )
    private void afterConstructor(MenuType<?> menuType, int id, Inventory ip, IPatternTerminalMenuHost host, boolean bindInventory, CallbackInfo ci) {
        this.eae_$toolbox = new ToolboxMenu(this);
        this.setupUpgrades(((IUpgradeableObject) host).getUpgrades());
        if (host.isUpgradedWith(ExpItems.PATTERN_REFILLER_CARD)) {
            var patternsLeft = blankPatternSlot.getItem().getCount();
            this.blankPatternSlot.safeInsert(AEItems.BLANK_PATTERN.stack(64));
            this.blankPatternSlot.setChanged();
        }
    }

    /*protected void refillBlankPatterns(Slot slot) {
        if (Platform.isServer()) {
            ItemStack blanks = slot.getItem();
            int blanksToRefill = 64;
            if (blanks != null) blanksToRefill -= blanks.getMaxStackSize();
            if (blanksToRefill <= 0) return;
            var grid
            AEItemKey.of(blanks)
            final AEItemStack request = AEItemStack
                    .create(AEApi.instance().definitions().materials().blankPattern().maybeStack(blanksToRefill).get());
            final IAEItemStack extracted = Platform
                    .poweredExtraction(this.getPowerSource(), this.getCellInventory(), request, this.getActionSource());
            if (extracted != null) {
                if (blanks != null) blanks.stackSize += extracted.getStackSize();
                else {
                    blanks = extracted.getItemStack();
                }
                slot.putStack(blanks);
            }
        }
    }*/
    @Inject(method = "encode", at = @At("RETURN"),
            remap = false)
    public void encode(CallbackInfo ci) {
        if (this.blankPatternSlot.getItem().getCount() < 64) this.blankPatternSlot.set(AEItems.BLANK_PATTERN.stack(64));
    }
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public ToolboxMenu getToolbox() {
        return this.eae_$toolbox;
    }
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public IUpgradeInventory getUpgrades() {
        return ((IUpgradeableObject) this.encodingLogic).getUpgrades();
    }
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean hasUpgrade(ItemLike upgradeCard) {
        return getUpgrades().isInstalled(upgradeCard);
    }

    @Inject(
            method = "broadcastChanges",
            at = @At("TAIL")
    )
    public void tickToolbox(CallbackInfo ci) {
        this.eae_$toolbox.tick();
    }
}
