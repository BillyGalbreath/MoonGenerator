package net.pl3x.bukkit.moongenerator.listener;

import net.pl3x.bukkit.moongenerator.configuration.Config;
import net.pl3x.bukkit.moongenerator.generator.MoonChunkGenerator;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldInitEvent;

public class WorldListener implements Listener {
    @EventHandler
    public void onWorldLoad(WorldInitEvent event) {
        World world = event.getWorld();
        if (world.getGenerator() instanceof MoonChunkGenerator) {
            // always darkness
            world.setTime(14300);
            world.setStorm(false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        }
    }

    @EventHandler
    public void onJoinWorld(PlayerJoinEvent event) {
        World world = event.getPlayer().getWorld();
        if (world.getGenerator() instanceof MoonChunkGenerator) {
            event.getPlayer().setResourcePack(Config.RP_MOON, Config.RP_MOON_HASH);
        }
    }

    @EventHandler
    public void onChangedWorld(PlayerChangedWorldEvent event) {
        World world = event.getPlayer().getWorld();
        if (world.getGenerator() instanceof MoonChunkGenerator) {
            event.getPlayer().setResourcePack(Config.RP_MOON, Config.RP_MOON_HASH);
        } else if (event.getFrom().getGenerator() instanceof MoonChunkGenerator) {
            event.getPlayer().setResourcePack(Config.RP_EMPTY, Config.RP_EMPTY_HASH);
        }
    }
}
