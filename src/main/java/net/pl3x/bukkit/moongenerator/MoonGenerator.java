package net.pl3x.bukkit.moongenerator;


import net.pl3x.bukkit.moongenerator.command.CmdMoon;
import net.pl3x.bukkit.moongenerator.configuration.Config;
import net.pl3x.bukkit.moongenerator.configuration.Lang;
import net.pl3x.bukkit.moongenerator.generator.MoonChunkGenerator;
import net.pl3x.bukkit.moongenerator.listener.CreatureListener;
import net.pl3x.bukkit.moongenerator.listener.HelmetListener;
import net.pl3x.bukkit.moongenerator.listener.RocketListener;
import net.pl3x.bukkit.moongenerator.listener.WorldListener;
import net.pl3x.bukkit.moongenerator.task.GravityTask;
import net.pl3x.bukkit.moongenerator.task.OxygenTask;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;


public final class MoonGenerator extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Config.reload(this);
        Lang.reload(this);

        getServer().getPluginManager().registerEvents(new CreatureListener(), this);
        getServer().getPluginManager().registerEvents(new HelmetListener(), this);
        getServer().getPluginManager().registerEvents(new RocketListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);

        getCommand("moon").setExecutor(new CmdMoon(this));

        new GravityTask().runTaskTimer(this, 0L, 30L);
        new OxygenTask().runTaskTimer(this, 0L, 30L);
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String name, String id) {
        return new MoonChunkGenerator(this, name, id);
    }
}
