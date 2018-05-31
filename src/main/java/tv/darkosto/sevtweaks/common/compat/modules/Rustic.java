package tv.darkosto.sevtweaks.common.compat.modules;

import blusunrize.immersiveengineering.api.tool.BelljarHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import tv.darkosto.sevtweaks.common.compat.ICompat;

public class Rustic extends ICompat {
    @Override
    public void preInit() {
    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {
        if (Loader.isModLoaded("immersiveengineering")) {
            /*
                Tomato Crop
             */
            Item tomatoSeeds = Item.REGISTRY.getObject(new ResourceLocation("rustic", "tomato_seeds"));
            Item tomatoCrop = Item.REGISTRY.getObject(new ResourceLocation("rustic", "tomato"));
            Block cropBlock = Block.REGISTRY.getObject(new ResourceLocation("rustic", "tomato_crop"));

            if (tomatoSeeds != null && tomatoCrop != null && cropBlock != null) {
                BelljarHandler.cropHandler.register(new ItemStack(tomatoSeeds), new ItemStack[]{
                                new ItemStack(tomatoCrop, 1),
                                new ItemStack(tomatoSeeds, 1)},
                        Blocks.FARMLAND,
                        cropBlock.getDefaultState());
            }

            /*
                Chili Crop
             */
            Item chiliSeeds = Item.REGISTRY.getObject(new ResourceLocation("rustic", "chili_pepper_seeds"));
            Item chiliCrop = Item.REGISTRY.getObject(new ResourceLocation("rustic", "chili_pepper"));
            Block chiliBlock = Block.REGISTRY.getObject(new ResourceLocation("rustic", "chili_crop"));

            if (chiliSeeds != null && chiliCrop != null && chiliBlock != null) {
                BelljarHandler.cropHandler.register(new ItemStack(chiliSeeds), new ItemStack[]{
                                new ItemStack(chiliCrop, 1),
                                new ItemStack(chiliSeeds, 1)},
                        Blocks.FARMLAND,
                        chiliBlock.getDefaultState());
            }
        }
    }
}
