package tv.darkosto.sevtweaks.common.gamestages;

import me.desht.pneumaticcraft.common.entity.living.EntityDrone;
import net.darkhax.gamestages.event.GameStageEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PNCDroneStaging {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onStageCheck(GameStageEvent.Check event) {
        // If the EntityPlayer is a DroneFakePlayer allow them to have the Stage.
        if (event.getEntity() instanceof EntityDrone.DroneFakePlayer) {
            event.setHasStage(true);
        }
    }
}
