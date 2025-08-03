package lu.kolja.expandedae.mixin.compat.cosmic;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridNode;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.ITerminalHost;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableItem;
import appeng.core.definitions.AEItems;
import appeng.helpers.IMenuCraftingPacket;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.slot.RestrictedInputSlot;
import de.mari_023.ae2wtlib.wut.WUTHandler;
import lu.kolja.expandedae.definition.ExpItems;
import lu.kolja.expandedae.helper.misc.KeybindUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(value = PatternEncodingTermMenu.class, remap = false)
public abstract class MixinPatternEncodingTerminalMenuCosm extends MEStorageMenu implements IMenuCraftingPacket {
    @Final
    @Shadow
    @Mutable
    private RestrictedInputSlot encodedPatternSlot;

    @Shadow
    @Final
    private RestrictedInputSlot blankPatternSlot;

    protected MixinPatternEncodingTerminalMenuCosm(MenuType<?> menuType, int id, Inventory ip, ITerminalHost host) {
        super(menuType, id, ip, host);
    }

    @Inject(method = "encode", at = @At("RETURN"))
    private void encode(CallbackInfo ci) {
        final IGridNode node = this.getNetworkNode();
        AtomicReference<Player> player = new AtomicReference<>();
        this.getActionSource().player().ifPresent(player::set);
        if (encodedPatternSlot.getItem() != ItemStack.EMPTY) {
            var terminalItem = expandedae$getTerminalItem(player.get());
            if (terminalItem == null) return;
            if (terminalItem.getItem() instanceof IUpgradeableItem item) {
                IUpgradeInventory inventory = item.getUpgrades(player.get().getMainHandItem());
                if (!inventory.isInstalled(ExpItems.PATTERN_REFILLER_CARD)) return;
            }

            var blankPatternSlotCount = blankPatternSlot.getItem().getCount();
            if (node == null) return;
            int changed = (int) Objects.requireNonNull(node).getGrid().getStorageService().getInventory().extract(
                    AEItemKey.of(AEItems.BLANK_PATTERN),
                    64 - blankPatternSlotCount,
                    Actionable.MODULATE,
                    this.getActionSource()
            );
            blankPatternSlot.set(new ItemStack(AEItems.BLANK_PATTERN, blankPatternSlotCount + changed));
            blankPatternSlot.setChanged();
        }
    }

    @Unique
    @Nullable
    private ItemStack expandedae$getTerminalItem(Player player) {
        var locator = WUTHandler.findTerminal(player, "pattern_encoding");
        if (locator == null) return null;
        return WUTHandler.getItemStackFromLocator(player, locator);
    }
}