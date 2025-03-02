package lu.kolja.expandedae.network;

import com.glodblock.github.glodium.network.NetworkHandler;
import com.glodblock.github.glodium.network.packet.SGenericPacket;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.network.packet.SExpPatternInfo;

public class EAENetworkHandler extends NetworkHandler {
    public static final EAENetworkHandler INSTANCE = new EAENetworkHandler();
    public EAENetworkHandler() {
        super(Expandedae.MODID);
    }
    public void init() {
        registerPacket(SGenericPacket.class, SGenericPacket::new);
        registerPacket(SExpPatternInfo.class, SExpPatternInfo::new);
    }
}
