package net.pl3x.bukkit.moongenerator.generator.populator;

import net.pl3x.bukkit.moongenerator.MoonGenerator;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Random;

public class OreVeinPopulator extends AbstractPopulator {
    public OreVeinPopulator(MoonGenerator plugin, World world) {
        super(plugin, world);
    }

    @Override
    public void populate(World world, Random rand, Chunk chunk) {
        try {
            int amount = between(rand, 50, 100);
            for (int i = 0; i < amount; i++) {
                int X = rand.nextInt(16);
                int Z = rand.nextInt(16);

                float maxY = world.getHighestBlockYAt(X, Z);
                if (maxY > 60) {
                    maxY = 60;
                } else if (maxY < 24) {
                    maxY = 24;
                }

                int Y;
                Material ore;
                int propagation;

                int orePicker = rand.nextInt(101);
                if (orePicker > 90) {
                    ore = Material.DIAMOND_ORE;
                    propagation = between(rand, 0, 3);
                    Y = between(rand, 3, 30);
                } else if (orePicker > 65) {
                    ore = Material.GOLD_ORE;
                    propagation = between(rand, 1, 4);
                    Y = between(rand, 3, (int) (maxY * 0.6F));
                } else if (orePicker > 50) {
                    ore = Material.REDSTONE_ORE;
                    propagation = between(rand, 2, 5);
                    Y = between(rand, 1, (int) (maxY * 0.4F));
                } else if (orePicker > 35) {
                    ore = Material.IRON_ORE;
                    propagation = between(rand, 5, 10);
                    Y = between(rand, 3, (int) (maxY * 0.9F));
                } else if (orePicker > 25) {
                    ore = Material.LAPIS_ORE;
                    propagation = between(rand, 5, 10);
                    Y = between(rand, 3, (int) (maxY * 0.5F));
                } else {
                    ore = Material.COAL_ORE;
                    propagation = between(rand, 5, 10);
                    Y = between(rand, 1, (int) maxY);
                }

                if (orePicker == 6) {
                    ore = Material.EMERALD_ORE;
                    propagation = 1;
                    Y = between(rand, 1, (int) (maxY * 0.2));
                }

                Block block = chunk.getBlock(X, Y, Z);
                if (!block.getType().equals(Material.BEDROCK) && !block.getType().equals(Material.AIR)) {
                    setType(chunk, X, Y, Z, ore);
                }

                for (int prop = 0; prop <= propagation; prop++) {
                    int turn = rand.nextInt(3);
                    if (turn == 0) {
                        X = rand.nextBoolean() ? subtract(X) : add(X);
                    } else if (turn == 1) {
                        Y = rand.nextBoolean() ? subtract(Y) : add(Y);
                    } else {
                        Z = rand.nextBoolean() ? subtract(Z) : add(Z);
                    }

                    block = chunk.getBlock(X, Y, Z);
                    if (!block.getType().equals(Material.BEDROCK) && !block.getType().equals(Material.AIR)) {
                        setType(chunk, X, Y, Z, ore);
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private int add(int n) {
        return n == 15 ? 14 : n + 1;
    }

    private int subtract(int n) {
        return n == 0 ? 1 : n - 1;
    }
}
