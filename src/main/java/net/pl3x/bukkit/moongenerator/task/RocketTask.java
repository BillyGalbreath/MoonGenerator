package net.pl3x.bukkit.moongenerator.task;

import net.pl3x.bukkit.moongenerator.MoonGenerator;
import net.pl3x.bukkit.moongenerator.configuration.Config;
import net.pl3x.bukkit.moongenerator.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class RocketTask extends BukkitRunnable {
    private final MoonGenerator plugin;
    private final Player player;
    private final Firework rocket;

    public RocketTask(MoonGenerator plugin, Player player, Firework rocket) {
        this.plugin = plugin;
        this.player = player;
        this.rocket = rocket;
    }

    @Override
    public void run() {
        if (!rocket.equals(player.getVehicle())) {
            // rocket went boom or player ejected
            Lang.sendActionBar(player, Lang.ROCKET_MISSION_FAILED
                    .replace("{height}", String.valueOf(Config.ROCKET_HEIGHT_REQUIREMENT)));
            player.setMetadata("no-fall-damage", new FixedMetadataValue(plugin, true));
            cancel();
            return;
        }

        // show current altitude
        int y = (int) player.getLocation().getY();
        player.sendActionBar(ChatColor.DARK_GREEN + "Y: " + y);

        if (rocket.getTicksLived() < ((rocket.getFireworkMeta().getPower() + 1) * 10) - 5) {
            return; // rocket is not near explosion time yet
        }

        if (y >= Config.ROCKET_HEIGHT_REQUIREMENT) {
            // success! height requirement reached
            cancel();
            int x = (int) player.getLocation().getX();
            int z = (int) player.getLocation().getZ();

            // lets go \o/
            if (Config.isMoon(player.getWorld())) {
                teleport(Config.EARTH_WORLD, x * Config.MOON_SCALE, z * Config.MOON_SCALE, Lang.ROCKET_EARTH_SUCCESS);
            } else {
                teleport(Config.MOON_WORLD, x / Config.MOON_SCALE, z / Config.MOON_SCALE, Lang.ROCKET_MOON_SUCCESS);
            }
        }
    }

    private void teleport(String worldName, int x, int z, String successMessage) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            Lang.sendActionBar(player, successMessage);
            player.teleportAsync(new Location(world, x, 255, z))
                    .thenAccept(success -> player.setMetadata("no-fall-damage", new FixedMetadataValue(plugin, true)));
        } else {
            Lang.sendActionBar(player, Lang.ROCKET_WORLD_NOT_FOUND
                    .replace("{world}", worldName));
        }
    }
}
