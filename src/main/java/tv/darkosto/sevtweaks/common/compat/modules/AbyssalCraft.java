package tv.darkosto.sevtweaks.common.compat.modules;

import com.shinoow.abyssalcraft.common.structures.StructureShoggothPit;
import com.shinoow.abyssalcraft.init.InitHandler;
import com.shinoow.abyssalcraft.lib.ACConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tv.darkosto.sevtweaks.SevTweaks;
import tv.darkosto.sevtweaks.common.compat.ICompat;
import tv.darkosto.sevtweaks.common.config.Configuration;

import java.util.Random;

public class AbyssalCraft extends ICompat {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        SevTweaks.logger.info("Registering ocean Shoggoth Lair generator");
        GameRegistry.registerWorldGenerator(new ShoggothLairOceanGenerator(), 0);
    }

    @Override
    public void postInit() {

    }

    static class ShoggothLairOceanGenerator implements IWorldGenerator {
        @Override
        public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
            boolean blacklisted = InitHandler.INSTANCE.isDimBlacklistedFromStructureGen(world.provider.getDimension());

            if (world.provider instanceof WorldProviderSurface && ACConfig.generateShoggothLairs && !blacklisted) {
                BlockPos pos = world.getHeight(new BlockPos(
                        chunkX * 16 + random.nextInt(16),
                        0,
                        chunkZ * 16 + random.nextInt(16)
                ));
                if (!BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.OCEAN)) return;
                if (!world.getBlockState(pos.down()).getMaterial().isLiquid()) return;
                if (Configuration.worldGen.shoggothOceanSpawnRate == 0 || random.nextInt(Configuration.worldGen.shoggothOceanSpawnRate) != 0)
                    return;
                new StructureShoggothPit().generate(world, random, pos);
            }
        }
    }
}
