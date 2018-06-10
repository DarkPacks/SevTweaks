package tv.darkosto.sevtweaks.common.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tv.darkosto.sevtweaks.common.util.References;

@EventBusSubscriber
@Config(modid = References.modID)
public class Configuration {
    public static CanceledEvents canceledEvents = new CanceledEvents();
    public static ShearedCreeperItemShead creeperItemShead = new ShearedCreeperItemShead();

    public static class CanceledEvents {
        @Config.Comment("Should the fishing rod event be cancelled. Thus not allowing any loot to be harvested.")
        @Config.Name("Cancel Fishing Rod Event")
        @Config.RequiresMcRestart
        public boolean cancelFishingRodEvent = true;
    }

    public static class ShearedCreeperItemShead {
        @Config.Comment("Should the Creeper drop the item defined.")
        @Config.Name("Enable Creeper Shead")
        @Config.RequiresMcRestart
        public boolean shouldShead = false;

        @Config.Comment("The item that the Creeper should shead after X time.")
        @Config.Name("Item to shead")
        public String sheadItem = "minecraft:book";

        @Config.Comment("The amount of the items that the Creeper should shead after X time.")
        @Config.Name("Amount to shead")
        public int sheadAmount = 1;

        @Config.Comment("Listing of the dimensions that the creeper won't shead the item.")
        @Config.Name("Blacklisted Dimensions")
        public Integer[] blacklistedDimensions = new Integer[]{ -1 };
    }

    @SubscribeEvent
    public static void onConfigChangeEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (!event.getModID().equals(References.modID)) {
            return;
        }
        ConfigManager.sync(References.modID, Config.Type.INSTANCE);
    }
}
