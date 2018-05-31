package tv.darkosto.sevtweaks.common.events;

import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tv.darkosto.sevtweaks.common.config.Configuration;

public class CanceledEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemFished(ItemFishedEvent event) {
        if (Configuration.canceledEvents.cancelFishingRodEvent) {
            event.setCanceled(true);
        }
    }
}
