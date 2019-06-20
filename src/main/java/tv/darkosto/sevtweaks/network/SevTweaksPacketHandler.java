package tv.darkosto.sevtweaks.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import tv.darkosto.sevtweaks.common.util.References;

public class SevTweaksPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(References.modID);
    
    public static void init() {
        INSTANCE.registerMessage(MarkersSyncPacket.MessageHandler.class, MarkersSyncPacket.class, 0, Side.CLIENT);
    }
}
