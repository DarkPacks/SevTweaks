package tv.darkosto.sevtweaks.common.compat.modules;

import com.google.common.collect.EvictingQueue;
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
        final EvictingQueue<BlockPos> lairPositions = EvictingQueue.create(30);

        @Override
        public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
            boolean blacklisted = InitHandler.INSTANCE.isDimBlacklistedFromStructureGen(world.provider.getDimension());

            if (world.provider instanceof WorldProviderSurface && ACConfig.generateShoggothLairs && !blacklisted) {
                if (Configuration.worldGen.shoggothOceanSpawnRate == 0 || random.nextInt(Configuration.worldGen.shoggothOceanSpawnRate) != 0)
                    return;

                for (int i = 0; i < 32; i++) {
                    BlockPos pos = world.getHeight(new BlockPos(
                            chunkX * 16 + random.nextInt(16),
                            0,
                            chunkZ * 16 + random.nextInt(16)
                    ));

                    if (!BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.OCEAN)) continue;

                    if (world.getBlockState(pos.down()).getMaterial().isLiquid()) pos = pos.down();
                    else continue;

                    if (world.isAirBlock(pos.north(13)) || world.isAirBlock(pos.north(20)) || world.isAirBlock(pos.north(27)))
                        continue;

                    BlockPos finalPos = pos;
                    if (lairPositions.stream().anyMatch(pos1 -> pos1.distanceSq(finalPos) < 16384)) continue;

                    lairPositions.add(pos);
                    new StructureShoggothPit().generate(world, random, pos);
                    break;
                }
            }
        }
    }
}
