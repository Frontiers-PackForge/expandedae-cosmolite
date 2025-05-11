package lu.kolja.expandedae.packets;

import appeng.core.sync.BasePacket;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lu.kolja.expandedae.block.entity.FilterContainerGroup;
import lu.kolja.expandedae.screen.FilterTermScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FilterTerminalPacket extends BasePacket {
    private boolean fullUpdate;
    private long inventoryId;
    private int inventorySize;
    private long sortBy;
    private FilterContainerGroup group;
    private Int2ObjectMap<ItemStack> slots;

    public FilterTerminalPacket(FriendlyByteBuf stream) {
        this.inventoryId = stream.readVarLong();
        this.fullUpdate = stream.readBoolean();
        if (this.fullUpdate) {
            this.inventorySize = stream.readVarInt();
            this.sortBy = stream.readVarLong();
            this.group = FilterContainerGroup.readFromPacket(stream);
        }
        int slotsCount = stream.readVarInt();
        this.slots = new Int2ObjectArrayMap<>(slotsCount);

        for(int i = 0; i < slotsCount; ++i) {
            int slot = stream.readVarInt();
            ItemStack item = stream.readItem();
            this.slots.put(slot, item);
        }
    }

    private FilterTerminalPacket(boolean fullUpdate, long inventoryId, int inventorySize, long sortBy, FilterContainerGroup group, Int2ObjectMap<ItemStack> slots) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer(2048));
        data.writeInt(this.getPacketID());
        data.writeVarLong(inventoryId);
        data.writeBoolean(fullUpdate);
        if (fullUpdate) {
            data.writeVarInt(inventorySize);
            data.writeVarLong(sortBy);
            group.writeToPacket(data);
        }

        data.writeVarInt(slots.size());

        for (Int2ObjectMap.Entry<ItemStack> itemStackEntry : slots.int2ObjectEntrySet()) {
            data.writeVarInt(itemStackEntry.getIntKey());
            data.writeItem((ItemStack) ((Int2ObjectMap.Entry<?>) itemStackEntry).getValue());
        }
        this.configureWrite(data);
    }

    public static FilterTerminalPacket fullUpdate(long inventoryId, int inventorySize, long sortBy, FilterContainerGroup group, Int2ObjectMap<ItemStack> slots) {
        return new FilterTerminalPacket(true, inventoryId, inventorySize, sortBy, group, slots);
    }

    public static FilterTerminalPacket incrementalUpdate(long inventoryId, Int2ObjectMap<ItemStack> slots) {
        return new FilterTerminalPacket(false, inventoryId, 0, 0L, null, slots);
    }

    @OnlyIn(Dist.CLIENT)
    public void clientPacketData(Player player) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof FilterTermScreen<?> filterTermScreen) {
            if (this.fullUpdate) {
                filterTermScreen.postFullUpdate(this.inventoryId, this.sortBy, this.group, this.inventorySize, this.slots);
            } else {
                filterTermScreen.postIncrementalUpdate(this.inventoryId, this.slots);
            }
        }
    }
}
