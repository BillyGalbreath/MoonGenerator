package net.pl3x.bukkit.moongenerator.listener;

import net.pl3x.bukkit.moongenerator.generator.MoonChunkGenerator;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public class WorldListener implements Listener {
    @EventHandler
    public void onWorldLoad(WorldInitEvent event) {
        World world = event.getWorld();
        if (world.getGenerator() instanceof MoonChunkGenerator) {
            // always darkness
            world.setTime(18000);
            world.setStorm(true);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setKeepSpawnInMemory(true);
        }
    }
}
