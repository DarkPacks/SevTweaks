package tv.darkosto.sevtweaks.common.compat.modules;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import pokefenn.totemic.api.ceremony.Ceremony;
import pokefenn.totemic.ceremony.*;
import pokefenn.totemic.init.ModContent;
import tv.darkosto.sevtweaks.common.compat.ICompat;
import tv.darkosto.sevtweaks.common.config.Configuration;

public class Totemic extends ICompat {
    @Override
    public void preInit() {
    }
    
    @Override
    public void init() {
        IForgeRegistry<Ceremony> ceremonyRegistry = GameRegistry.findRegistry(Ceremony.class);
        ceremonyRegistry.registerAll(
                ModContent.warDance = new CeremonyWarDance("totemic:warDance",
                        Configuration.ceremonies.warDanceMusic, Configuration.ceremonies.warDanceMusic * 20,
                        ModContent.drum, ModContent.drum).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "war_dance"),
                ModContent.depths = new CeremonyDepths("totemic:depths",
                        Configuration.ceremonies.depthsMusic, Configuration.ceremonies.depthsStartTime * 20,
                        ModContent.flute, ModContent.flute).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "depths"),
                ModContent.fertility = new CeremonyFertility("totemic:fertility",
                        Configuration.ceremonies.fertilityMusic, Configuration.ceremonies.fertilityStartTime * 20,
                        ModContent.flute, ModContent.drum).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "fertility"),
                ModContent.zaphkielWaltz = new CeremonyZaphkielWaltz("totemic:zaphkielWaltz",
                        Configuration.ceremonies.zaphkielWaltzMusic, Configuration.ceremonies.zaphkielWaltzStartTime * 20,
                        ModContent.windChime, ModContent.flute).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "zaphkiel_waltz"),
                ModContent.buffaloDance = new CeremonyBuffaloDance("totemic:buffaloDance",
                        Configuration.ceremonies.buffaloDanceDanceMusic, Configuration.ceremonies.buffaloDanceStartTime * 20,
                        ModContent.drum, ModContent.windChime).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "buffalo_dance"),
                ModContent.rainDance = new CeremonyRain(true, "totemic:rainDance",
                        Configuration.ceremonies.rainDanceMusic, Configuration.ceremonies.rainDanceStartTime * 20,
                        ModContent.drum, ModContent.rattle).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "rain_dance"),
                ModContent.drought = new CeremonyRain(false, "totemic:drought",
                        Configuration.ceremonies.droughtMusic, Configuration.ceremonies.droughtStartTime * 20,
                        ModContent.rattle, ModContent.drum).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "drought"),
                ModContent.fluteCeremony = new CeremonyFluteInfusion("totemic:flute",
                        Configuration.ceremonies.fluteMusic, Configuration.ceremonies.fluteStartTime * 20,
                        ModContent.flute, ModContent.rattle).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "flute"),
                ModContent.eagleDance = new CeremonyEagleDance("totemic:eagleDance",
                        Configuration.ceremonies.eagleDanceMusic, Configuration.ceremonies.eagleDanceStartTime * 20,
                        ModContent.rattle, ModContent.windChime).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "eagle_dance"),
                ModContent.cleansing = new CeremonyCleansing("totemic:cleansing",
                        Configuration.ceremonies.cleansingMusic, Configuration.ceremonies.cleansingStartTime * 20,
                        ModContent.eagleBoneWhistle, ModContent.flute).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "cleansing"),
                ModContent.baykokSummon = new CeremonyBaykok("totemic:baykokSummon",
                        Configuration.ceremonies.baykokSummonMusic, Configuration.ceremonies.baykokSummonStartTime * 20,
                        ModContent.windChime, ModContent.eagleBoneWhistle).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "baykok_summon"),
                ModContent.sunDance = new CeremonySunDance("totemic:sunDance",
                        Configuration.ceremonies.sunDanceMusic, Configuration.ceremonies.sunDanceStartTime * 20,
                        ModContent.drum, ModContent.eagleBoneWhistle).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "sun_dance"),
                ModContent.danseMacabre = new CeremonyDanseMacabre("totemic:danseMacabre",
                        Configuration.ceremonies.danseMacabreMusic, Configuration.ceremonies.sunDanceStartTime * 20,
                        ModContent.eagleBoneWhistle, ModContent.windChime).setRegistryName(pokefenn.totemic.Totemic.MOD_ID, "danseMacabre"));
    }
    
    @Override
    public void postInit() {
    
    }
}
