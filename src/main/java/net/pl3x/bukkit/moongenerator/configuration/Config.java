package net.pl3x.bukkit.moongenerator.configuration;

import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.base.Throwables;
import com.mojang.authlib.GameProfile;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Level;

public class Config {
    public static String LANGUAGE_FILE = "lang-en.yml";

    public static String EARTH_WORLD = "world";
    public static String MOON_WORLD = "moon";

    public static int MOON_SCALE = 8;

    public static boolean USE_GRAVITY = true;
    public static boolean USE_PARTICLES = true;

    public static boolean USE_GLASS_HELMETS = true;
    public static ItemStack HELMET = defaultHelmet();

    public static boolean USE_ROCKETS = true;
    public static int ROCKET_HEIGHT_REQUIREMENT = 120;
    public static ItemStack ROCKET = defaultRocket();

    public static boolean USE_OXYGEN = true;
    public static int OXYGEN_RADIUS = 10;
    public static boolean USE_OXYGEN_SAFE_ZONES = true;

    public static String RP_MOON = "https://pl3x.net/resource_packs/Moon.zip";
    public static String RP_MOON_HASH = "04CF2F4185D360E9644B334D8D8F40BB5F858879";
    public static String RP_EMPTY = "https://pl3x.net/resource_packs/Empty.zip";
    public static String RP_EMPTY_HASH = "BE986C44653397E617BADEC097870D0D4F7B3E1B";

    private static void init() {
        LANGUAGE_FILE = getString("language-file", LANGUAGE_FILE);

        EARTH_WORLD = getString("earth-world", EARTH_WORLD);
        MOON_WORLD = getString("moon-world", MOON_WORLD);

        MOON_SCALE = getInt("moon-scale", MOON_SCALE);

        USE_GRAVITY = getBoolean("use-gravity", USE_GRAVITY);

        USE_PARTICLES = getBoolean("use-particles", USE_PARTICLES);

        USE_GLASS_HELMETS = getBoolean("use-glass-helmets", USE_GLASS_HELMETS);
        HELMET = getItemStack("helmet", HELMET);

        USE_ROCKETS = getBoolean("use-rockets", USE_ROCKETS);
        ROCKET_HEIGHT_REQUIREMENT = getInt("rocket-height-requirement", ROCKET_HEIGHT_REQUIREMENT);
        ROCKET = getItemStack("rocket", ROCKET);

        USE_OXYGEN = getBoolean("use-oxygen", USE_OXYGEN);
        OXYGEN_RADIUS = getInt("oxygen-radius", OXYGEN_RADIUS);
        USE_OXYGEN_SAFE_ZONES = getBoolean("use-oxygen-safe-zones", USE_OXYGEN_SAFE_ZONES);

        RP_MOON = getString("resource-pack.moon.url", RP_MOON);
        RP_MOON_HASH = getString("resource-pack.moon.hash", RP_MOON_HASH);
        RP_EMPTY = getString("resource-pack.empty.url", RP_EMPTY);
        RP_EMPTY_HASH = getString("resource-pack.empty.hash", RP_EMPTY_HASH);
    }

    private static ItemStack defaultRocket() {
        ItemStack rocket = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta meta = rocket.getItemMeta();
        meta.setDisplayName("Moon Rocket");
        meta.setLore(Collections.singletonList("Space exploration"));
        rocket.setItemMeta(meta);
        return rocket;
    }

    private static ItemStack defaultHelmet() {
        ItemStack helmet = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) helmet.getItemMeta();
        meta.setDisplayName("Moon Helmet");
        meta.setLore(Collections.singletonList("Space exploration"));
        PlayerProfile profile = CraftPlayerProfile.asBukkitMirror(new GameProfile(UUID.fromString("167a4b6d-9fe4-4687-8993-5cec916c6be9"), "Moon Helmet"));
        profile.setProperty(new ProfileProperty("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGRkMDhiZjM4YmQxYjIzM2U1ZDRkYzVkY2FhMDQ0YzI2NzI0NDU3NzU0MjIxZjUwM2ZkYmE1MThlNGIyNWE5ZSJ9fX0="));
        meta.setPlayerProfile(profile);
        helmet.setItemMeta(meta);
        return helmet;
    }

    // ############################  DO NOT EDIT BELOW THIS LINE  ############################

    /**
     * Reload the configuration file
     */
    public static void reload(Plugin plugin) {
        GLASS_BLOCKS = new MaterialSetTag(new NamespacedKey(plugin, "glass_blocks")).endsWith("_GLASS").add(Material.GLASS);

        //plugin.saveDefaultConfig();
        configFile = new File(plugin.getDataFolder(), "config.yml");
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException ignore) {
        } catch (InvalidConfigurationException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not load config.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header("This is the configuration file for " + plugin.getName());
        config.options().copyDefaults(true);

        Config.init();

        save(plugin);
    }

    private static void save(Plugin plugin) {
        try {
            config.save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save " + configFile, ex);
        }
    }

    private static YamlConfiguration config;
    private static File configFile;

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private static ItemStack getItemStack(String path, ItemStack def) {
        config.addDefault(path, def);
        return config.getItemStack(path, config.getItemStack(path));
    }

    public static boolean isRocket(ItemStack itemStack) {
        return itemStack != null && itemStack.isSimilar(ROCKET);
    }

    private static MaterialSetTag GLASS_BLOCKS;

    public static boolean isGlassHelmet(ItemStack itemStack) {
        return itemStack != null && GLASS_BLOCKS.isTagged(itemStack);
    }

    public static boolean isEarth(World world) {
        return world != null && world.getName().equals(EARTH_WORLD);
    }

    public static boolean isMoon(World world) {
        return world != null && world.getName().equals(MOON_WORLD);
    }

    public static void saveHelmet(Plugin plugin, ItemStack helmet) {
        HELMET = helmet;
        config.addDefault("helmet", helmet);
        config.set("helmet", helmet);
        save(plugin);
    }

    public static void saveRocket(Plugin plugin, ItemStack rocket) {
        ROCKET = rocket;
        config.addDefault("rocket", rocket);
        config.set("rocket", rocket);
        save(plugin);
    }
}
