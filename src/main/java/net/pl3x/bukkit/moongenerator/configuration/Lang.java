package net.pl3x.bukkit.moongenerator.configuration;

import com.google.common.base.Throwables;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Lang {
    public static String COMMAND_NO_PERMISSION = "&4You do not have permission for that command!";
    public static String PLAYER_COMMAND = "&4This command is only available to players!";
    public static String PLAYER_NOT_FOUND = "&cPlayer not found";

    public static String ROCKET_COUNTDOWN = "&c3 2 1...!";

    public static String ROCKET_WORLD_NOT_FOUND = "&cWorld '{world}' not found";
    public static String ROCKET_MISSION_FAILED = "&cMission Failed...! You didn't get to Y {height}";
    public static String ROCKET_MOON_SUCCESS = "&aMission Accomplished...! Traveling to the Moon";
    public static String ROCKET_EARTH_SUCCESS = "&aMission Accomplished...! Traveling back to Earth";

    public static final String RECEIVED_HELMET = "&aYou have received a helmet!";
    public static final String RECEIVED_ROCKET = "&aYou have received a rocket!";

    public static final String GAVE_HELMET = "&aYou have given {target} a helmet!";
    public static final String GAVE_ROCKET = "&aYou have given {target} a rocket!";

    public static final String SAVED_HELMET = "&aHelmet has been set";
    public static final String SAVED_ROCKET = "&aRocket has been set";

    public static final String NOT_A_HELMET = "&cThat is not a valid helmet";
    public static final String NOT_A_ROCKET = "&cThat is not a valid rocket";

    private static void init() {
        COMMAND_NO_PERMISSION = getString("command-no-permission", COMMAND_NO_PERMISSION);
        PLAYER_COMMAND = getString("player-command", PLAYER_COMMAND);
    }

    // ############################  DO NOT EDIT BELOW THIS LINE  ############################

    /**
     * Reload the language file
     */
    public static void reload(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), Config.LANGUAGE_FILE);
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load " + Config.LANGUAGE_FILE + ", please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header("This is the main language file for " + plugin);
        config.options().copyDefaults(true);

        Lang.init();

        try {
            config.save(configFile);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + configFile, ex);
        }
    }

    private static YamlConfiguration config;

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    /**
     * Sends a message to a recipient
     *
     * @param recipient Recipient of message
     * @param message   Message to send
     */
    public static void send(CommandSender recipient, String message) {
        if (recipient != null) {
            for (String part : colorize(message).split("\n")) {
                recipient.sendMessage(part);
            }
        }
    }

    /**
     * Send a message to a player's actionbar
     *
     * @param recipient Player to send to
     * @param message   Message to send
     */
    public static void sendActionBar(Player recipient, String message) {
        if (recipient != null) {
            recipient.sendActionBar(colorize(message));
        }
    }

    /**
     * Broadcast a message to server
     *
     * @param message Message to broadcast
     */
    public static void broadcast(String message) {
        for (String part : colorize(message).split("\n")) {
            Bukkit.getOnlinePlayers().forEach(recipient -> recipient.sendMessage(part));
            Bukkit.getConsoleSender().sendMessage(part);
        }
    }

    /**
     * Colorize a String
     *
     * @param str String to colorize
     * @return Colorized String
     */
    public static String colorize(String str) {
        if (str == null) {
            return "";
        }
        str = ChatColor.translateAlternateColorCodes('&', str);
        if (ChatColor.stripColor(str).isEmpty()) {
            return "";
        }
        return str;
    }
}
