package net.pl3x.bukkit.moongenerator.task;

import net.pl3x.bukkit.moongenerator.configuration.Config;
import net.pl3x.bukkit.moongenerator.generator.MoonChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GravityTask extends BukkitRunnable {
    @Override
    public void run() {
        if (Config.USE_GRAVITY) {
            Bukkit.getWorlds().stream()
                    .filter(world -> world.getGenerator() instanceof MoonChunkGenerator)
                    .forEach(world -> world.getPlayers().stream()
                            .filter(player -> player.getGameMode() != GameMode.SPECTATOR)
                            .forEach(this::applyLowGravity)
                    );
        }
    }

    private void applyLowGravity(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 40, 3), true);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 3), true);
    }
}
