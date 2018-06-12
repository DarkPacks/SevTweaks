package tv.darkosto.sevtweaks;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import tv.darkosto.sevtweaks.common.command.CommandSevTweaks;
import tv.darkosto.sevtweaks.common.compat.Compat;
import tv.darkosto.sevtweaks.common.events.CanceledEvents;
import tv.darkosto.sevtweaks.common.gamestages.GameStageScoreboard;
import tv.darkosto.sevtweaks.common.util.References;

@Mod(modid = References.modID, name = References.modName, version = References.modVersion,
        acceptedMinecraftVersions = References.mcVersion,
        dependencies =
                "required-after:crafttweaker@[1.12-4.1.8.470,];" +
                        "after:guideapi@[1.12-2.1.4-57,];after:itemstages@[2.0.35,];" +
                        "after:gamestages@[2.0.91,];after:dimstages@[2.0.20,];after:tinkerstages@[2.0.15,];" +
                        "after:mobstages@[2.0.8,];"
)
public class SevTweaks {

    public static Logger logger;
    public static GameStageScoreboard scoreboard = new GameStageScoreboard();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        Compat.compactPreInit();
        MinecraftForge.EVENT_BUS.register(CanceledEvents.class);
        if (Loader.isModLoaded("gamestages")) {
            MinecraftForge.EVENT_BUS.register(GameStageScoreboard.class);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Compat.compactInit();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Compat.compactPostInit();
    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSevTweaks());
    }
}
