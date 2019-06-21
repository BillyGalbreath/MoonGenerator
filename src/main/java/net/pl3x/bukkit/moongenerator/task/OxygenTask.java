package net.pl3x.bukkit.moongenerator.task;

import net.pl3x.bukkit.moongenerator.configuration.Config;
import net.pl3x.bukkit.moongenerator.generator.MoonChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class OxygenTask extends BukkitRunnable {
    @Override
    public void run() {
        if (Config.USE_OXYGEN) {
            Bukkit.getWorlds().stream()
                    .filter(world -> world.getGenerator() instanceof MoonChunkGenerator)
                    .forEach(world -> world.getPlayers().stream()
                            .filter(player -> player.getGameMode() != GameMode.SPECTATOR)
                            .filter(this::notNearOxygen)
                            .forEach(this::applyNoOxygen)
                    );
        }
    }

    private boolean notNearOxygen(Player player) {
        if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
            return false; // creative and spectator mode players always have oxygen
        }

        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet != null) {
            if (helmet.isSimilar(Config.HELMET)) {
                return false; // wearing helmet
            }
            if (Config.isGlassHelmet(helmet)) {
                return false; // wearing glass helmet
            }
        }

        if (!Config.USE_OXYGEN_SAFE_ZONES) {
            return true; // safe zones are disabled
        }

        Block block = player.getLocation().getBlock();
        for (int x = Config.OXYGEN_RADIUS; x >= -Config.OXYGEN_RADIUS; x--) {
            for (int y = Config.OXYGEN_RADIUS; y >= -Config.OXYGEN_RADIUS; y--) {
                for (int z = Config.OXYGEN_RADIUS; z >= -Config.OXYGEN_RADIUS; z--) {
                    Material material = block.getRelative(x, y, z).getType();
                    if (Tag.LEAVES.isTagged(material) || material.equals(Material.SPONGE)) {
                        return false; // is near a safe zone
                    }
                }
            }
        }

        return true; // not near a safe zone
    }

    private void applyNoOxygen(Player player) {
        PotionEffect witherEffect = new PotionEffect(PotionEffectType.WITHER, 40, 1);
        player.addPotionEffect(witherEffect, true);
        if (Config.USE_PARTICLES) {
            player.spawnParticle(Particle.DAMAGE_INDICATOR, player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ(), 10);
        }
        player.playSound(player.getLocation(), Sound.ENTITY_DROWNED_DEATH, 1f, 1f);
    }
}
