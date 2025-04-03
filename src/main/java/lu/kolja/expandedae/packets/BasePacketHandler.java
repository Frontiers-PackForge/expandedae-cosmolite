package lu.kolja.expandedae.packets;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import appeng.core.sync.BasePacket;
import net.minecraft.network.FriendlyByteBuf;

public class BasePacketHandler {
    private static final Map<Class<? extends BasePacket>, PacketTypes> REVERSE_LOOKUP = new HashMap<>();

    public enum PacketTypes {
        FILTER_CLEAR(ClearFilterTerminalPacket.class, ClearFilterTerminalPacket::new),
        FILTER_UPDATE(FilterTerminalPacket.class, FilterTerminalPacket::new);

        private final Function<FriendlyByteBuf, BasePacket> factory;

        PacketTypes(Class<? extends BasePacket> packetClass, Function<FriendlyByteBuf, BasePacket> factory) {
            this.factory = factory;

            REVERSE_LOOKUP.put(packetClass, this);
        }

        public static PacketTypes getPacket(int id) {
            return values()[id];
        }

        public int getPacketId() {
            return ordinal();
        }

        static PacketTypes getID(Class<? extends BasePacket> c) {
            return REVERSE_LOOKUP.get(c);
        }

        public BasePacket parsePacket(FriendlyByteBuf in) throws IllegalArgumentException {
            return this.factory.apply(in);
        }
    }
}
