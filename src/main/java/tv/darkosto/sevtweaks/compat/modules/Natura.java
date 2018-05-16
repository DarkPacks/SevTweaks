package tv.darkosto.sevtweaks.compat.modules;

import blusunrize.immersiveengineering.api.tool.BelljarHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import tv.darkosto.sevtweaks.compat.ICompat;

public class Natura extends ICompat {
    @Override
    public void preInit() {
    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {
        if (Loader.isModLoaded("immersiveengineering")) {
            Item cottonSeeds = Item.REGISTRY.getObject(new ResourceLocation("natura", "overworld_seeds"));
            Item cottonCrop = Item.REGISTRY.getObject(new ResourceLocation("natura", "materials"));
            Block cottonBlock = Block.REGISTRY.getObject(new ResourceLocation("natura", "cotton_crop"));

            if (cottonSeeds != null && cottonCrop != null && cottonBlock != null) {
                BelljarHandler.cropHandler.register(new ItemStack(cottonSeeds, 1, 1), new ItemStack[]{
                                new ItemStack(cottonCrop, 1, 3),
                                new ItemStack(cottonSeeds, 1, 1)},
                        new ItemStack(Blocks.FARMLAND),
                        cottonBlock.getDefaultState());
            }
        }
    }
}
