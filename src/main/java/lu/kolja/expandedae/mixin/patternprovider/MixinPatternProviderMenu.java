package lu.kolja.expandedae.mixin.patternprovider;

import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantics;
import appeng.menu.implementations.PatternProviderMenu;
import lu.kolja.expandedae.helper.base.IUpgradableMenu;
import lu.kolja.expandedae.helper.misc.PatternHelper;
import lu.kolja.expandedae.helper.patternprovider.IPatternProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PatternProviderMenu.class, remap = false)
public abstract class MixinPatternProviderMenu extends AEBaseMenu implements IUpgradableMenu, IPatternProvider {

    public MixinPatternProviderMenu(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
        super(menuType, id, playerInventory, host);
    }

    @Inject(
            method = "<init>(Lnet/minecraft/world/inventory/MenuType;ILnet/minecraft/world/entity/player/Inventory;Lappeng/helpers/patternprovider/PatternProviderLogicHost;)V",
            at = @At("TAIL"),
            remap = false
    )
    private void initToolbox(MenuType<?> menuType, int id, Inventory playerInventory, PatternProviderLogicHost host, CallbackInfo ci) {
        this.registerClientAction("modifyPatterns", Integer.class, this::expandedae$modifyPatterns);
    }

    @Unique
    @Override
    public void expandedae$modifyPatterns(Integer mult) {
        if (this.isClientSide()) this.sendClientAction("modifyPatterns", mult);
        else {
            for (var slot : this.getSlots(SlotSemantics.ENCODED_PATTERN)) {
                slot.set(PatternHelper.modifyPatterns(slot.getItem(), mult, this.getPlayer().level()));
            }
        }
    }
}