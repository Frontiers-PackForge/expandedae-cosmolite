package lu.kolja.expandedae.block.entity;

import appeng.api.inventories.InternalInventory;
import appeng.api.inventories.ItemTransfer;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.stacks.AEItemKey;
import appeng.core.localization.GuiText;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record FilterContainerGroup(@Nullable AEItemKey icon, Component name, List<Component> tooltip) {
    private static final FilterContainerGroup NOTHING;

    public FilterContainerGroup(@Nullable AEItemKey icon, Component name, List<Component> tooltip) {
        this.icon = icon;
        this.name = name;
        this.tooltip = tooltip;
    }

    public static FilterContainerGroup nothing() {
        return NOTHING;
    }

    public void writeToPacket(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.icon != null);
        if (this.icon != null) {
            this.icon.writeToPacket(buffer);
        }
        buffer.writeComponent(this.name);
        buffer.writeVarInt(this.tooltip.size());
        for(Component component : this.tooltip) {
            buffer.writeComponent(component);
        }
    }

    public static FilterContainerGroup readFromPacket(FriendlyByteBuf buffer) {
        AEItemKey icon = buffer.readBoolean() ? AEItemKey.fromPacket(buffer) : null;
        Component name = buffer.readComponent();
        int lineCount = buffer.readVarInt();
        ArrayList<Component> lines = new ArrayList<>(lineCount);
        for (int i = 0; i < lineCount; i++) {
            lines.add(buffer.readComponent());
        }
        return new FilterContainerGroup(icon, name, lines);
    }
    public static @Nullable FilterContainerGroup fromMachine(Level level, BlockPos pos, Direction side) {
        BlockEntity target = level.getBlockEntity(pos);
        if (target == null) {
            return null;
        } else {
            ItemTransfer adaptor = InternalInventory.wrapExternal(target, side);
            if (adaptor != null && adaptor.mayAllowInsertion()) {
                List<Component> tooltip = List.of();
                AEItemKey icon;
                Component name;
                if (target instanceof IPartHost partHost) {
                    IPart part = partHost.getPart(side);
                    if (part == null) {
                        return null;
                    }
                    icon = AEItemKey.of(part.getPartItem());
                    if (part instanceof Nameable nameable) {
                        name = nameable.getDisplayName();
                    } else {
                        name = icon.getDisplayName();
                    }
                } else {
                    Block targetBlock = target.getBlockState().getBlock();
                    ItemStack targetItem = new ItemStack(targetBlock);
                    icon = AEItemKey.of(targetItem);
                    if (target instanceof Nameable nameable) {
                        if (nameable.hasCustomName()) {
                            name = nameable.getCustomName();
                            return new FilterContainerGroup(icon, name, tooltip);
                        }
                    }
                    if (targetItem.isEmpty()) {
                        return null;
                    }
                    name = targetItem.getHoverName();
                }
                return new FilterContainerGroup(icon, name, tooltip);
            } else {
                return null;
            }
        }
    }
    public @Nullable AEItemKey icon() {
        return this.icon;
    }

    public Component name() {
        return this.name;
    }

    public List<Component> tooltip() {
        return this.tooltip;
    }

    static {
        NOTHING = new FilterContainerGroup(AEItemKey.of(Items.AIR), GuiText.Nothing.text(), List.of());
    }
}
