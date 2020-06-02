package tv.darkosto.sevtweaks.common.compat.modules;

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
    private int baseDruidFrequency;

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
        private Set<Long> circleLocations = new HashSet<>();

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
            long hashedPos = (long) altar.getX() >> 7 | (long) altar.getZ() >> 7 << 32;
            if (!circleLocations.contains(hashedPos)) {
                super.generateStructure(world, rand, altar);
                circleLocations.add(hashedPos);
            }
        }
    }
}
