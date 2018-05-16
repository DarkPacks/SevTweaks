package tv.darkosto.sevtweaks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import tv.darkosto.sevtweaks.common.util.References;
import tv.darkosto.sevtweaks.compat.Compat;

@Mod(modid = References.modID, name = References.modName, version = References.modVersion,
        acceptedMinecraftVersions = References.mcVersion)
public class SevTweaks {
    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        Compat.compactPreInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Loaded SevTweaks!");
        Compat.compactInit();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Compat.compactPostInit();
    }
}
