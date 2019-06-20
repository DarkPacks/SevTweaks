package tv.darkosto.sevtweaks.common.compat;

import net.minecraftforge.fml.common.Loader;
import tv.darkosto.sevtweaks.SevTweaks;
import tv.darkosto.sevtweaks.common.compat.modules.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Based of the Immersive Engineering Compat Loader/System.
 * <p>
 * See: https://github.com/BluSunrize/ImmersiveEngineering/tree/master/src/main/java/blusunrize/immersiveengineering/common/util/compat
 */
public class Compat {
    private static HashMap<String, Class<? extends ICompat>> compatModules = new HashMap<>();
    private static Set<ICompat> modules = new HashSet<>();

    static {
        compatModules.put("natura", Natura.class);
        compatModules.put("rustic", Rustic.class);
        compatModules.put("betterwithmods", BetterWithMods.class);
        compatModules.put("galacticraftplanets", GalacticraftPlanets.class);
        compatModules.put("antiqueatlas", AaToJmWaypoints.class);
        compatModules.put("totemic", Totemic.class);
    }

    public static void compactPreInit() {
        for (Map.Entry<String, Class<? extends ICompat>> e : compatModules.entrySet()) {
            if (Loader.isModLoaded(e.getKey())) {
                try {
                    ICompat compat = e.getValue().newInstance();
                    modules.add(compat);
                    compat.preInit();
                } catch (Exception el) {
                    SevTweaks.logger.error("Compat module for " + e.getKey() + " could not be preInitialized.");
                }
            }
        }
    }

    public static void compactInit() {
        for (ICompat compat : Compat.modules) {
            try {
                compat.init();
            } catch (Exception el) {
                SevTweaks.logger.error("Compat module for " + compat + " could not be initialized.", el);
            }
        }
    }

    public static void compactPostInit() {
        for (ICompat compat : Compat.modules) {
            try {
                compat.postInit();
            } catch (Exception el) {
                SevTweaks.logger.error("Compat module for " + compat + " could not be postInitialized.", el);
            }
        }
    }
}
