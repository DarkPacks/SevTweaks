package tv.darkosto.sevtweaks.common.compat.modules;

import hunternif.mc.atlas.api.AtlasAPI;
import hunternif.mc.atlas.marker.DimensionMarkersData;
import hunternif.mc.atlas.marker.Marker;
import hunternif.mc.atlas.marker.MarkersData;
import journeymap.client.model.Waypoint;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import net.darkhax.gamestages.event.GameStageEvent;
import net.darkhax.jmapstages.JMapStages;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tv.darkosto.sevtweaks.SevTweaks;
import tv.darkosto.sevtweaks.common.compat.ICompat;
import tv.darkosto.sevtweaks.network.MarkersSyncPacket;
import tv.darkosto.sevtweaks.network.SevTweaksPacketHandler;

import java.awt.*;
import java.util.*;

public class AaToJmWaypoints extends ICompat {
    /**
     * If Added Game Stage is the JMapStages waypoints stage, copy all Antique Atlas markers to JMap waypoints
     */
    @SubscribeEvent
    public void gamestageEvent(GameStageEvent.Added event) {
        if (event.getEntity().world.isRemote) {
            return; // Shouldn't happen but protect against changes in game stages
        }
        if (event.getStageName().equals(JMapStages.stageWaypoint)) {
            Collection<Marker> markers = getAaMarkers(event.getEntityPlayer());
            
            if (markers == null || markers.isEmpty()) {
                return;
            }
    
            SevTweaksPacketHandler.INSTANCE.sendTo(new MarkersSyncPacket(markers), (EntityPlayerMP)event.getEntityPlayer());
        }
    }
    
    /**
     * Gather the markers from the Markers save data on the server side
     * @param player Check this player's atlases (from inventory)
     * @return
     */
    public static Collection<Marker> getAaMarkers(EntityPlayer player) {
        if (player.world.isRemote) return null;
        
        // Make set to prevent duplicate markers in case player has multiple of the same atlas
        Set<Integer> playerAtlases = new HashSet<>(AtlasAPI.getPlayerAtlases(player));
        Collection<Marker> markers = new ArrayList<>();
        
        for (int atlasId : playerAtlases) {
            MarkersData markersData = (MarkersData)player.world.loadData(MarkersData.class, String.format("aaMarkers_%d", atlasId));
            if (markersData == null) continue;
            Set<Integer> visitedDimensions = markersData.getVisitedDimensions();
            
            for (int dimension : visitedDimensions) {
                DimensionMarkersData dimensionMarkersData = markersData.getMarkersDataInDimension(dimension);
                Collection<Marker> markersInDimension = dimensionMarkersData.getAllMarkers();
                markers.addAll(markersInDimension);
            }
        }
        return markers;
    }
    
    /**
     * Add received markers to JMap as waypoints on the client
     * @param markers Collection of Markers to be added to JMap
     */
    @SideOnly(Side.CLIENT)
    public static void doSync(Collection<Marker> markers) {
        SevTweaks.logger.info("{} markers from Antique Atlas are being imported into JourneyMap", markers.size());
        
        Random random = new Random();
        
        for (Marker marker : markers) {
            WaypointStore.INSTANCE.add(new Waypoint(
                    marker.getLocalizedLabel(),
                    new BlockPos(marker.getX(), 70, marker.getZ()),
                    marker.getType().equals("antiqueatlas:nether_portal") ? new Color(134, 0, 175) : new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()),
                    marker.getType().equals("antiqueatlas:tomb") ? Waypoint.Type.Death : Waypoint.Type.Normal,
                    marker.getDimension()
            ).setDirty());
        }
        WaypointStore.INSTANCE.bulkSave();
    }
    
    @Override
    public void preInit() {
        if (!Loader.isModLoaded(Journeymap.MOD_ID) || !Loader.isModLoaded("jmapstages")) return;
        MinecraftForge.EVENT_BUS.register(this);
        
    }
    
    @Override
    public void init() {
    
    }
    
    @Override
    public void postInit() {
    
    }
}
