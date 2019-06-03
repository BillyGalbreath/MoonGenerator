package net.pl3x.bukkit.moongenerator.generator.populator;

import net.pl3x.bukkit.moongenerator.MoonGenerator;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Random;

public class FloraPopulator extends AbstractPopulator {
    public FloraPopulator(MoonGenerator plugin, World world) {
        super(plugin, world);
    }

    @Override
    public void populate(World world, Random rand, Chunk chunk) {
        if (rand.nextBoolean()) {
            return;
        }
        int amount = rand.nextInt(5) + 1;
        for (int i = 1; i < amount; i++) {
            int X = rand.nextInt(16);
            int Z = rand.nextInt(16);
            int Y = 1;

            for (int j = world.getMaxHeight() - 1; chunk.getBlock(X, j, Z).getType() == Material.AIR; j--) {
                Y = j;
            }

            int chance = rand.nextInt(101);
            if (chance > 97) {
                setType(chunk, X, Y, Z, Material.DEAD_FIRE_CORAL_FAN);
                setType(chunk, X, Y - 1, Z, Material.ANDESITE);
                setType(chunk, X, Y - 2, Z, Material.DIAMOND_ORE);
            } else if (chance > 86) {
                setType(chunk, X, Y, Z, Material.DEAD_BUBBLE_CORAL);
                setType(chunk, X, Y - 1, Z, Material.ANDESITE);
                setType(chunk, X, Y - 2, Z, Material.IRON_ORE);
            } else if (chance > 64) {
                setType(chunk, X, Y, Z, Material.DEAD_TUBE_CORAL);
                setType(chunk, X, Y - 1, Z, Material.ANDESITE);
                setType(chunk, X, Y - 2, Z, Material.COAL_ORE);
            } else if (chance > 54) {
                setType(chunk, X, Y, Z, Material.DEAD_TUBE_CORAL_FAN);
                setType(chunk, X, Y - 1, Z, Material.ANDESITE);
                if (rand.nextBoolean()) {
                    setType(chunk, X, Y - 2, Z, Material.COBBLESTONE);
                }
            } else if (chance > 34) {
                setType(chunk, X, Y, Z, Material.DEAD_HORN_CORAL_FAN);
                setType(chunk, X, Y - 1, Z, Material.ANDESITE);
                if (rand.nextBoolean()) {
                    setType(chunk, X, Y - 2, Z, Material.COBBLESTONE);
                }
            } else {
                setType(chunk, X, Y, Z, Material.DEAD_FIRE_CORAL_FAN);
                setType(chunk, X, Y - 1, Z, Material.ANDESITE);
                if (rand.nextBoolean()) {
                    setType(chunk, X, Y, Z, Material.DEAD_TUBE_CORAL_FAN);
                }
                if (rand.nextBoolean()) {
                    setType(chunk, X, Y - 2, Z, Material.COBBLESTONE);
                }
            }
        }
    }
}
