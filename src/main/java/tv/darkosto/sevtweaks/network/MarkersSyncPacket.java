package tv.darkosto.sevtweaks.network;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import hunternif.mc.atlas.marker.Marker;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tv.darkosto.sevtweaks.common.compat.modules.AaToJmWaypoints;

import java.util.ArrayList;
import java.util.Collection;

public class MarkersSyncPacket implements IMessage {
    Collection<Marker> markers;
    
    public MarkersSyncPacket() {
        this.markers = new ArrayList<>();
    }
    
    public MarkersSyncPacket(Collection<Marker> markers) {
        this.markers = markers;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        
        int typeCount = packetBuffer.readVarInt();
        for (int i = 0; i < typeCount; i++) {
            String typeName = ByteBufUtils.readUTF8String(packetBuffer);
            int markerCount = packetBuffer.readVarInt();
            for (int j = 0; j < markerCount; j++) {
                markers.add(new Marker(j, typeName,
                        ByteBufUtils.readUTF8String(packetBuffer), // Label
                        packetBuffer.readVarInt(), // Dimension
                        packetBuffer.readVarInt(), // X
                        packetBuffer.readVarInt(), // Z
                        true));
            }
        }
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        
        // Sort Markers by their type
        ListMultimap<String, Marker> markerTypeMap = ArrayListMultimap.create();
        for (Marker marker : markers) {
            markerTypeMap.put(marker.getType(), marker);
        }
        
        // Send number of types
        packetBuffer.writeVarInt(markerTypeMap.keySet().size());
        
        for (String type : markerTypeMap.keySet()) {
            // Send type name - used to colour the waypoints in JM - and count of this type
            ByteBufUtils.writeUTF8String(packetBuffer, type);
            packetBuffer.writeVarInt(markerTypeMap.get(type).size());
            for (Marker marker : markerTypeMap.get(type)) {
                // Send Label and Coords
                ByteBufUtils.writeUTF8String(packetBuffer, marker.getLabel());
                packetBuffer.writeVarInt(marker.getDimension());
                packetBuffer.writeVarInt(marker.getX());
                packetBuffer.writeVarInt(marker.getZ());
            }
        }
        
        
    }
    
    public static class MessageHandler implements IMessageHandler<MarkersSyncPacket, IMessage> {
        @Override
        public IMessage onMessage(MarkersSyncPacket message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> AaToJmWaypoints.doSync(message.markers));
            return null;
        }
    }
}
