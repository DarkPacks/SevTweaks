package tv.darkosto.sevtweaks.common.compat.modules;

import betterwithmods.common.entity.EntityShearedCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tv.darkosto.sevtweaks.common.config.Configuration;
import tv.darkosto.sevtweaks.common.compat.ICompat;

import java.util.Arrays;
import java.util.Random;

public class BetterWithMods extends ICompat {
    private static int timeUntilNextDrop = new Random().nextInt(6000) + 6000;

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent event) {
        if (event.getEntityLiving() == null || event.getEntityLiving().world == null) {
            return;
        }
        Entity entity = event.getEntity();
        World world = event.getEntityLiving().world;
        if (Configuration.creeperItemShead.blacklistedDimensions.length > 0) {
            if (Arrays.asList(Configuration.creeperItemShead.blacklistedDimensions).contains(world.getWorldType().getId())) {
                return;
            }
        }
        if (Configuration.creeperItemShead.sheadItem.length() < 0) {
            return;
        }
        if (Configuration.creeperItemShead.shouldShead && !world.isRemote && entity instanceof EntityShearedCreeper && --timeUntilNextDrop <= 0) {
            String[] item = Configuration.creeperItemShead.sheadItem.split(":");
            Item registryObject = Item.REGISTRY.getObject(new ResourceLocation(item[0], item[1]));
            if (registryObject == null) {
                return;
            }
            ItemStack itemToDrop = new ItemStack(registryObject, Configuration.creeperItemShead.sheadAmount);
            if (item.length > 2) {
                itemToDrop = new ItemStack(registryObject, Configuration.creeperItemShead.sheadAmount, Integer.parseInt(item[2]));
            }
            entity.entityDropItem(itemToDrop, 0.9f);
            timeUntilNextDrop = new Random().nextInt(6000) + 6000;
        }
    }
}