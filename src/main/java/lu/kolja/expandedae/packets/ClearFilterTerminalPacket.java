package lu.kolja.expandedae.packets;

import lu.kolja.expandedae.screen.FilterTermScreen;
import appeng.core.sync.BasePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import io.netty.buffer.Unpooled;

public class ClearFilterTerminalPacket extends BasePacket {
    public ClearFilterTerminalPacket(FriendlyByteBuf stream) {
    }

    public ClearFilterTerminalPacket() {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer(16));
        data.writeInt(this.getPacketID());
        this.configureWrite(data);
    }

    @OnlyIn(Dist.CLIENT)
    public void clientPacketData(Player player) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof FilterTermScreen<?> filterTermScreen) {
            filterTermScreen.clear();
        }
    }
}
