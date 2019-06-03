package net.pl3x.bukkit.moongenerator.generator;

import net.minecraft.server.v1_14_R1.MathHelper;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class CaveGenerator extends BaseGenerator {
    private static final Material LAVA = Material.LAVA;
    private static final Material AIR = Material.AIR;

    CaveGenerator(MoonChunkGenerator generator) {
        super(generator);
    }

    private void addRoom(long seed, int originalX, int originalZ, ChunkGenerator.ChunkData chunkData, double x, double y, double z) {
        addTunnel(seed, originalX, originalZ, chunkData, x, y, z, 1.0F + rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    private void addTunnel(long seed, int originalX, int originalZ, ChunkGenerator.ChunkData chunkData, double x, double y, double z, float p_180702_12_, float p_180702_13_, float p_180702_14_, int p_180702_15_, int p_180702_16_, double p_180702_17_) {
        double d0 = (double) (originalX * 16 + 8);
        double d1 = (double) (originalZ * 16 + 8);
        float f = 0.0F;
        float f1 = 0.0F;
        Random random = new Random(seed);

        if (p_180702_16_ <= 0) {
            int i = range * 16 - 16;
            p_180702_16_ = i - random.nextInt(i / 4);
        }

        boolean flag2 = false;

        if (p_180702_15_ == -1) {
            p_180702_15_ = p_180702_16_ / 2;
            flag2 = true;
        }

        int j = random.nextInt(p_180702_16_ / 2) + p_180702_16_ / 4;

        for (boolean flag = random.nextInt(6) == 0; p_180702_15_ < p_180702_16_; ++p_180702_15_) {
            double d2 = 1.5D + (double) (MathHelper.sin((float) p_180702_15_ * (float) Math.PI / (float) p_180702_16_) * p_180702_12_);
            double d3 = d2 * p_180702_17_;
            float f2 = MathHelper.cos(p_180702_14_);
            float f3 = MathHelper.sin(p_180702_14_);
            x += (double) (MathHelper.cos(p_180702_13_) * f2);
            y += (double) f3;
            z += (double) (MathHelper.sin(p_180702_13_) * f2);

            if (flag) {
                p_180702_14_ = p_180702_14_ * 0.92F;
            } else {
                p_180702_14_ = p_180702_14_ * 0.7F;
            }

            p_180702_14_ = p_180702_14_ + f1 * 0.1F;
            p_180702_13_ += f * 0.1F;
            f1 = f1 * 0.9F;
            f = f * 0.75F;
            f1 = f1 + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f = f + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

            if (!flag2 && p_180702_15_ == j && p_180702_12_ > 1.0F && p_180702_16_ > 0) {
                addTunnel(random.nextLong(), originalX, originalZ, chunkData, x, y, z, random.nextFloat() * 0.5F + 0.5F, p_180702_13_ - ((float) Math.PI / 2F), p_180702_14_ / 3.0F, p_180702_15_, p_180702_16_, 1.0D);
                addTunnel(random.nextLong(), originalX, originalZ, chunkData, x, y, z, random.nextFloat() * 0.5F + 0.5F, p_180702_13_ + ((float) Math.PI / 2F), p_180702_14_ / 3.0F, p_180702_15_, p_180702_16_, 1.0D);
                return;
            }

            if (flag2 || random.nextInt(4) != 0) {
                double d4 = x - d0;
                double d5 = z - d1;
                double d6 = (double) (p_180702_16_ - p_180702_15_);
                double d7 = (double) (p_180702_12_ + 2.0F + 16.0F);

                if (d4 * d4 + d5 * d5 - d6 * d6 > d7 * d7) {
                    return;
                }

                if (x >= d0 - 16.0D - d2 * 2.0D && z >= d1 - 16.0D - d2 * 2.0D && x <= d0 + 16.0D + d2 * 2.0D && z <= d1 + 16.0D + d2 * 2.0D) {
                    int k2 = MathHelper.floor(x - d2) - originalX * 16 - 1;
                    int k = MathHelper.floor(x + d2) - originalX * 16 + 1;
                    int l2 = MathHelper.floor(y - d3) - 1;
                    int l = MathHelper.floor(y + d3) + 1;
                    int i3 = MathHelper.floor(z - d2) - originalZ * 16 - 1;
                    int i1 = MathHelper.floor(z + d2) - originalZ * 16 + 1;

                    if (k2 < 0) {
                        k2 = 0;
                    }

                    if (k > 16) {
                        k = 16;
                    }

                    if (l2 < 1) {
                        l2 = 1;
                    }

                    if (l > 248) {
                        l = 248;
                    }

                    if (i3 < 0) {
                        i3 = 0;
                    }

                    if (i1 > 16) {
                        i1 = 16;
                    }
                    for (int j3 = k2; j3 < k; ++j3) {
                        double d10 = ((double) (j3 + originalX * 16) + 0.5D - x) / d2;

                        for (int i2 = i3; i2 < i1; ++i2) {
                            double d8 = ((double) (i2 + originalZ * 16) + 0.5D - z) / d2;
                            boolean reachedTop = false;

                            if (d10 * d10 + d8 * d8 < 1.0D) {
                                for (int j2 = l; j2 > l2; --j2) {
                                    double d9 = ((double) (j2 - 1) + 0.5D - y) / d3;

                                    if (d9 > -0.7D && d10 * d10 + d9 * d9 + d8 * d8 < 1.0D) {
                                        BlockData data1 = chunkData.getBlockData(j3, j2, i2);

                                        if (data1.getMaterial() == Material.DEAD_BRAIN_CORAL_BLOCK) {
                                            reachedTop = true;
                                        }

                                        if (j2 - 1 < 10) {
                                            chunkData.setBlock(j3, j2, i2, LAVA);
                                        } else {
                                            chunkData.setBlock(j3, j2, i2, AIR);
                                            if (reachedTop && chunkData.getBlockData(j3, j2 - 1, i2).getMaterial() == Material.DEAD_BUBBLE_CORAL_BLOCK) {
                                                //BlockPosition pos = new BlockPosition(j3 + originalX * 16, 0, i2 + originalZ * 16);
                                                chunkData.setBlock(j3, j2 - 1, i2, Material.SPONGE);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (flag2) {
                        break;
                    }
                }
            }
        }
    }

    protected void recursiveGenerate(World world, int chunkX, int chunkZ, int originalX, int originalZ, ChunkGenerator.ChunkData chunkData) {
        int i = rand.nextInt(rand.nextInt(rand.nextInt(15) + 1) + 1);

        if (rand.nextInt(7) != 0) {
            i = 0;
        }

        for (int j = 0; j < i; ++j) {
            double d0 = (double) (chunkX * 16 + rand.nextInt(16));
            double d1 = (double) rand.nextInt(rand.nextInt(120) + 8);
            double d2 = (double) (chunkZ * 16 + rand.nextInt(16));
            int k = 1;

            if (rand.nextInt(4) == 0) {
                addRoom(rand.nextLong(), originalX, originalZ, chunkData, d0, d1, d2);
                k += rand.nextInt(4);
            }

            for (int l = 0; l < k; ++l) {
                float f = rand.nextFloat() * ((float) Math.PI * 2F);
                float f1 = (rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float f2 = rand.nextFloat() * 2.0F + rand.nextFloat();

                if (rand.nextInt(10) == 0) {
                    f2 *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
                }

                addTunnel(rand.nextLong(), originalX, originalZ, chunkData, d0, d1, d2, f2, f, f1, 0, 0, 1.0D);
            }
        }
    }
}
