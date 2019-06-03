package net.pl3x.bukkit.moongenerator.generator.populator;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.pl3x.bukkit.moongenerator.MoonGenerator;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.craftbukkit.v1_14_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_14_R1.block.data.CraftBlockData;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

abstract class AbstractPopulator extends BlockPopulator {
    protected final MoonGenerator plugin;
    protected final World world;

    AbstractPopulator(MoonGenerator plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }

    int between(Random rand, int min, int max) {
        if (min >= max) {
            return rand.nextInt((min - max) + 1) + max;
        } else {
            return rand.nextInt((max - min) + 1) + min;
        }
    }

    void setType(Chunk chunk, int x, int y, int z, Material material) {
        //chunk.getBlock(x, y, z).setType(material); // DO NOT USE! Loads chunks infinitely

        BlockData data = material.createBlockData();
        if (data instanceof Waterlogged) {
            ((Waterlogged) data).setWaterlogged(false);
        }
        ((CraftChunk) chunk).getHandle().setType(
                new BlockPosition(x, y, z),
                ((CraftBlockData) data).getState(),
                false, // isMoving
                true // doPlace
        );
    }
}
