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
    public static GameStageScoreboard gamestageScoreboard = new GameStageScoreboard();
    public static CrashData crashData = new CrashData();
    public static Debuggers debuggers = new Debuggers();
    public static WorldGen worldGen = new WorldGen();
    public static TotemicCeremonies ceremonies = new TotemicCeremonies();

    @SubscribeEvent
    public static void onConfigChangeEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (!event.getModID().equals(References.modID)) {
            return;
        }
        ConfigManager.sync(References.modID, Config.Type.INSTANCE);
    }

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
        public Integer[] blacklistedDimensions = new Integer[]{-1};
    }

    public static class GameStageScoreboard {
        @Config.Comment("The position for the scoreboard. Valid: 0 is tab menu, 1 is sidebar, 2 is below name.")
        @Config.Name("Scoreboard Position")
        public int scoreboardPosition = 2;
    }

    public static class CrashData {
        @Config.Comment("The name of the modpack to use as the Crash Label")
        @Config.Name("Modpack Name")
        public String name = "SevTech: Ages";

        @Config.Comment("The version of the modpack")
        @Config.Name("Modpack Version")
        public String version = "0.0.0";
    }

    public static class Debuggers {
        @Config.Comment("Create a biomes.md file displaying each biome's name, registry name, and ID")
        @Config.Name("Create Biome Table File")
        @Config.RequiresMcRestart
        public boolean createBiomeFile = false;
    }

    public static class WorldGen {
        @Config.Comment("Generation chance of a Shoggoth Lair in ocean biomes. Higher numbers decrease the chance of a Lair generating, while lower numbers increase the chance. 0 disables.")
        @Config.Name("Shoggoth Lair Generation Chance: Oceans")
        @Config.RangeInt(min = 0, max = 1000)
        public int shoggothOceanSpawnRate = 3;
    }
    
    public static class TotemicCeremonies {
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the War Dance ceremony")
        @Config.Name("War Dance Startup Time")
        @Config.RequiresMcRestart
        public int warDanceStartTime = 20;
        
        @Config.Comment("The amount of music required for the player to start the War Dance ceremony")
        @Config.Name("War Dance Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int warDanceMusic = 75;
    
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the Symphony of the Depths ceremony")
        @Config.Name("Depths Startup Time")
        @Config.RequiresMcRestart
        public int depthsStartTime = 20;
    
        @Config.Comment("The amount of music required for the player to start the Symphony of the Depths ceremony")
        @Config.Name("Depths Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int depthsMusic = 75;
    
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the Rite of Spring ceremony")
        @Config.Name("Fertility Startup Time")
        @Config.RequiresMcRestart
        public int fertilityStartTime = 23;
    
        @Config.Comment("The amount of music required for the player to start the Rite of Spring ceremony")
        @Config.Name("Fertility Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int fertilityMusic = 88;
    
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the Zaphkiel Waltz ceremony")
        @Config.Name("Zaphkiel Waltz Startup Time")
        @Config.RequiresMcRestart
        public int zaphkielWaltzStartTime = 20;
    
        @Config.Comment("The amount of music required for the player to start the Zaphkiel Waltz ceremony")
        @Config.Name("Zaphkiel Waltz Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int zaphkielWaltzMusic = 112;
    
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the Buffalo Dance ceremony")
        @Config.Name("Buffalo Dance Startup Time")
        @Config.RequiresMcRestart
        public int buffaloDanceStartTime = 24;
    
        @Config.Comment("The amount of music required for the player to start the Buffalo Dance ceremony")
        @Config.Name("Buffalo Dance Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int buffaloDanceDanceMusic = 123;
    
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the Rain Dance ceremony")
        @Config.Name("Rain Dance Startup Time")
        @Config.RequiresMcRestart
        public int rainDanceStartTime = 26;
    
        @Config.Comment("The amount of music required for the player to start the Rain Dance ceremony")
        @Config.Name("Rain Dance Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int rainDanceMusic = 183;
        
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the Drought ceremony")
        @Config.Name("Drought Startup Time")
        @Config.RequiresMcRestart
        public int droughtStartTime = 26;
    
        @Config.Comment("The amount of music required for the player to start the Drought ceremony")
        @Config.Name("Drought Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int droughtMusic = 183;
    
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the Serenade for Syrinx ceremony")
        @Config.Name("Flute Startup Time")
        @Config.RequiresMcRestart
        public int fluteStartTime = 28;
    
        @Config.Comment("The amount of music required for the player to start the Serenade for Syrinx ceremony")
        @Config.Name("Flute Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int fluteMusic = 189;
    
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the Eagle Dance ceremony")
        @Config.Name("Eagle Dance Startup Time")
        @Config.RequiresMcRestart
        public int eagleDanceStartTime = 25;
    
        @Config.Comment("The amount of music required for the player to start the Eagle Dance ceremony")
        @Config.Name("Eagle Dance Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int eagleDanceMusic = 193;
    
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the Steve's Lullaby ceremony")
        @Config.Name("Cleansing Startup Time")
        @Config.RequiresMcRestart
        public int cleansingStartTime = 30;
    
        @Config.Comment("The amount of music required for the player to start the Steve's Lullaby ceremony")
        @Config.Name("Cleansing Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int cleansingMusic = 245;
    
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the Totentanz ceremony")
        @Config.Name("Baykok Summoning Startup Time")
        @Config.RequiresMcRestart
        public int baykokSummonStartTime = 32;
    
        @Config.Comment("The amount of music required for the player to start the Totentanz ceremony")
        @Config.Name("Baykok Summoning Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int baykokSummonMusic = 251;
    
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the Sun Dance ceremony")
        @Config.Name("Sun Dance Startup Time")
        @Config.RequiresMcRestart
        public int sunDanceStartTime = 31;
    
        @Config.Comment("The amount of music required for the player to start the Sun Dance ceremony")
        @Config.Name("Sun Dance Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int sunDanceMusic = 247;
    
        @Config.Comment("The time allowed (in seconds) for the player to accumulate enough music to start the Danse Macabre ceremony")
        @Config.Name("Danse Macabre Startup Time")
        @Config.RequiresMcRestart
        public int danseMacabreStartTime = 32;
    
        @Config.Comment("The amount of music required for the player to start the Danse Macabre ceremony")
        @Config.Name("Danse Macabre Music")
        @Config.RequiresMcRestart
        @Config.RangeInt(min = 0, max = 270)
        public int danseMacabreMusic = 249;
    }
}
