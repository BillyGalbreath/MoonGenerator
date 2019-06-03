package net.pl3x.bukkit.moongenerator.generator;

import net.pl3x.bukkit.moongenerator.MoonGenerator;
import net.pl3x.bukkit.moongenerator.generator.populator.FloraPopulator;
import net.pl3x.bukkit.moongenerator.generator.populator.OreVeinPopulator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MoonChunkGenerator extends ChunkGenerator {
    protected final MoonGenerator plugin;
    private final String name;
    private final String id;

    private final CaveGenerator caveGenerator;

    public MoonChunkGenerator(MoonGenerator plugin, String name, String id) {
        this.plugin = plugin;
        this.name = name;
        this.id = id;

        caveGenerator = new CaveGenerator(this);
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        int height;

        ChunkGenerator.ChunkData chunkData = createChunkData(world);

        SimplexOctaveGenerator chunkGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        chunkGenerator.setScale(0.006D);

        for (int X = 0; X < 16; X++) {
            for (int Z = 0; Z < 16; Z++) {

                height = (int) (chunkGenerator.noise(chunkX * 16 + X, chunkZ * 16 + Z, 0.06D, 0.8D, false) * 15D + 60D);

                if (height > 200) height = 200;
                if (height < 16) height = 16;


                chunkData.setBlock(X, height, Z, Material.DEAD_BRAIN_CORAL_BLOCK);
                if (random.nextInt(100) < 90) {
                    chunkData.setBlock(X, height - 1, Z, Material.DEAD_BUBBLE_CORAL_BLOCK);
                } else {
                    chunkData.setBlock(X, height - 1, Z, Material.DEAD_HORN_CORAL_BLOCK);
                }
                chunkData.setBlock(X, height - 2, Z, Material.DEAD_BUBBLE_CORAL_BLOCK);
                chunkData.setBlock(X, height - 3, Z, Material.DEAD_BUBBLE_CORAL_BLOCK);

                for (int i = 0; i < height - 3; i++) {
                    if (i > height * 0.8) {
                        chunkData.setBlock(X, i, Z, Material.DEAD_FIRE_CORAL_BLOCK);
                    } else if (i > height * 0.6) {
                        chunkData.setBlock(X, i, Z, Material.DEAD_TUBE_CORAL_BLOCK);
                    } else if (i > height * 0.4) {
                        chunkData.setBlock(X, i, Z, Material.COBBLESTONE);
                    } else if (i > height * 0.1) {
                        chunkData.setBlock(X, i, Z, Material.PACKED_ICE);
                    } else {
                        chunkData.setBlock(X, i, Z, Material.BLUE_ICE);
                    }
                    if (i <= 2) {
                        if (random.nextBoolean() && random.nextBoolean() && random.nextBoolean())
                            chunkData.setBlock(X, i, Z, Material.BEDROCK);
                    }

                }

                chunkData.setBlock(X, 0, Z, Material.BEDROCK);

                biome.setBiome(X, Z, Biome.ERODED_BADLANDS);
            }
        }

        caveGenerator.generate(world, chunkX, chunkZ, chunkData);

        return chunkData;
    }

    @Override
    public boolean isParallelCapable() {
        return true;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList(new OreVeinPopulator(plugin, world), new FloraPopulator(plugin, world));
    }
}
