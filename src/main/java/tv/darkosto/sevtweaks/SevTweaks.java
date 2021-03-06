package tv.darkosto.sevtweaks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import tv.darkosto.sevtweaks.common.command.CommandSevTweaks;
import tv.darkosto.sevtweaks.common.command.CommandWaypoints;
import tv.darkosto.sevtweaks.common.compat.Compat;
import tv.darkosto.sevtweaks.common.config.Configuration;
import tv.darkosto.sevtweaks.common.crash.PackCrashEnhancement;
import tv.darkosto.sevtweaks.common.events.CanceledEvents;
import tv.darkosto.sevtweaks.common.gamestages.GameStageScoreboard;
import tv.darkosto.sevtweaks.common.gamestages.PNCDroneStaging;
import tv.darkosto.sevtweaks.common.util.BiomeDebugTable;
import tv.darkosto.sevtweaks.common.util.References;
import tv.darkosto.sevtweaks.network.SevTweaksPacketHandler;

@Mod(modid = References.modID, name = References.modName, version = References.modVersion,
        acceptedMinecraftVersions = References.mcVersion,
        dependencies = "required-after:crafttweaker@[1.12-4.1.8.470,];" +
                "after:gamestages@[2.0.97,];after:galacticraftplanets@[4.0.1.181,];after:mekanism@[1.12.2-9.4.13.349,]"
)
public class SevTweaks {

    public static Logger logger;
    public static GameStageScoreboard scoreboard = new GameStageScoreboard();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        FMLCommonHandler.instance().registerCrashCallable(new PackCrashEnhancement());
    
        SevTweaksPacketHandler.init();

        Compat.compactPreInit();
        MinecraftForge.EVENT_BUS.register(CanceledEvents.class);
        if (Loader.isModLoaded("gamestages")) {
            MinecraftForge.EVENT_BUS.register(GameStageScoreboard.class);
            if (Loader.isModLoaded("pneumaticcraft")) {
                MinecraftForge.EVENT_BUS.register(PNCDroneStaging.class);
            }
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Compat.compactInit();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Compat.compactPostInit();

        if (Configuration.debuggers.createBiomeFile) {
            BiomeDebugTable.createFile();
        }
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSevTweaks());
        if (Loader.isModLoaded("antiqueatlas") && Loader.isModLoaded("journeymap")) {
            event.registerServerCommand(new CommandWaypoints());
        }
    }
}
