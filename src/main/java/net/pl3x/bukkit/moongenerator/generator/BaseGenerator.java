package net.pl3x.bukkit.moongenerator.generator;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class BaseGenerator {
    protected final MoonChunkGenerator generator;

    protected int range = 8;
    protected Random rand = new Random();
    protected World world;

    public BaseGenerator(MoonChunkGenerator generator) {
        this.generator = generator;
    }

    public void generate(World worldIn, int x, int z, ChunkGenerator.ChunkData chunkData) {
        int i = range;
        world = worldIn;
        rand.setSeed(worldIn.getSeed());
        long j = rand.nextLong();
        long k = rand.nextLong();

        for (int l = x - i; l <= x + i; ++l) {
            for (int i1 = z - i; i1 <= z + i; ++i1) {
                long j1 = (long) l * j;
                long k1 = (long) i1 * k;
                rand.setSeed(j1 ^ k1 ^ worldIn.getSeed());
                recursiveGenerate(worldIn, l, i1, x, z, chunkData);
            }
        }
    }

    public static void setupChunkSeed(long seed, Random rand, int x, int z) {
        rand.setSeed(seed);
        long i = rand.nextLong();
        long j = rand.nextLong();
        long k = (long) x * i;
        long l = (long) z * j;
        rand.setSeed(k ^ l ^ seed);
    }

    protected void recursiveGenerate(World world, int chunkX, int chunkZ, int originalX, int originalZ, ChunkGenerator.ChunkData chunkData) {
    }
}
