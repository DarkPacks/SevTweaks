package tv.darkosto.sevtweaks.common.command;

import hunternif.mc.atlas.marker.Marker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import tv.darkosto.sevtweaks.common.compat.modules.AaToJmWaypoints;
import tv.darkosto.sevtweaks.network.MarkersSyncPacket;
import tv.darkosto.sevtweaks.network.SevTweaksPacketHandler;

import java.util.Collection;

public class CommandWaypoints extends CommandBase {
    @Override
    public String getName() {
        return "waypoints";
    }
    
    @Override
    public String getUsage(ICommandSender sender) {
        return "/waypoints";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP)) return;
        
        Collection<Marker> markers = AaToJmWaypoints.getAaMarkers((EntityPlayer)sender);
        
        if (markers == null) return;
        
        sender.sendMessage(new TextComponentString(String.format("Importing %d markers from Antique Atlas to JourneyMap", markers.size())));
        
        SevTweaksPacketHandler.INSTANCE.sendTo(
                new MarkersSyncPacket(markers),
                (EntityPlayerMP)sender
        );
    }
}
