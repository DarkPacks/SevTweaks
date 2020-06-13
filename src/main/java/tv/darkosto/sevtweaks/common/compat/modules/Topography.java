package tv.darkosto.sevtweaks.common.compat.modules;

import com.google.common.collect.EvictingQueue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thebetweenlands.common.world.gen.feature.structure.WorldGenDruidCircle;
import tv.darkosto.sevtweaks.SevTweaks;
import tv.darkosto.sevtweaks.common.compat.ICompat;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Topography extends ICompat {
    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        SevTweaks.logger.info("Registering working Druid Circle generator");
        GameRegistry.registerWorldGenerator(new WorldGenDruidCircleAlt(), 0);
    }

    @Override
    public void postInit() {
    }

    static class WorldGenDruidCircleAlt extends WorldGenDruidCircle {
        final EvictingQueue<BlockPos> circlePositions = EvictingQueue.create(30);

        @Override
        public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
            if (world.provider.getDimension() == DimensionType.OVERWORLD.getId()) {
                try {
                    Method method = this.getClass().getSuperclass().getDeclaredMethod("generate", World.class, Random.class, int.class, int.class);
                    method.setAccessible(true);
                    method.invoke(this, world, random, chunkX << 4, chunkZ << 4);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        @Override
        public void generateStructure(World world, Random rand, BlockPos altar) {
            if (!circlePositions.stream().anyMatch(pos1 -> pos1.distanceSq(altar) < 16384)) {
                super.generateStructure(world, rand, altar);
                circlePositions.add(altar);
            }
        }
    }
}
