package tv.darkosto.sevtweaks.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import tv.darkosto.sevtweaks.common.compat.modules.AaToJmWaypoints;
import tv.darkosto.sevtweaks.network.MarkersSyncPacket;
import tv.darkosto.sevtweaks.network.SevTweaksPacketHandler;

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
        SevTweaksPacketHandler.INSTANCE.sendTo(new MarkersSyncPacket(
                AaToJmWaypoints.getAaMarkers((EntityPlayer)sender)),
                (EntityPlayerMP)sender
        );
    }
}
