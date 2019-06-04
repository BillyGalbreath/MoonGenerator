package net.pl3x.bukkit.moongenerator.command;

import net.pl3x.bukkit.moongenerator.MoonGenerator;
import net.pl3x.bukkit.moongenerator.configuration.Config;
import net.pl3x.bukkit.moongenerator.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CmdMoon implements TabExecutor {
    private final MoonGenerator plugin;

    public CmdMoon(MoonGenerator plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("command.moon")) {
            if (args.length == 1) {
                String arg = args[0].toLowerCase();
                return Stream.of("reload", "give", "set")
                        .filter(cmd -> cmd.startsWith(arg))
                        .collect(Collectors.toList());
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("set")) {
                    String arg = args[1].toLowerCase();
                    return Stream.of("helmet", "rocket")
                            .filter(cmd -> cmd.startsWith(arg))
                            .collect(Collectors.toList());
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (args[1].equalsIgnoreCase("helmet") || args[1].equalsIgnoreCase("rocket")) {
                        String arg = args[2].toLowerCase();
                        return Bukkit.getOnlinePlayers().stream()
                                .filter(player -> player.getName().toLowerCase().startsWith(arg))
                                .map(HumanEntity::getName)
                                .collect(Collectors.toList());
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("command.moon")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            Lang.send(sender, "&d" + plugin.getName() + " v" + plugin.getDescription().getVersion());
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                Config.reload(plugin);
                Lang.reload(plugin);
                Lang.send(sender, "&d" + plugin.getName() + " v" + plugin.getDescription().getVersion() + " reloaded");
                return true;
            }
            return false; // show usage
        }

        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                Lang.send(sender, Lang.PLAYER_COMMAND);
                return true;
            }

            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("give")) {
                if (args[1].equalsIgnoreCase("helmet")) {
                    // give self helmet
                    Lang.send(sender, Lang.RECEIVED_HELMET);
                    player.getInventory().addItem(Config.HELMET).forEach((Integer, overflow) ->
                            player.getWorld().dropItem(player.getLocation(), overflow)
                                    .setOwner(player.getUniqueId()));
                    return true;
                }

                if (args[1].equalsIgnoreCase("rocket")) {
                    // give self rocket
                    Lang.send(sender, Lang.RECEIVED_ROCKET);
                    player.getInventory().addItem(Config.ROCKET).forEach((Integer, overflow) ->
                            player.getWorld().dropItem(player.getLocation(), overflow)
                                    .setOwner(player.getUniqueId()));
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("set")) {
                if (args[1].equalsIgnoreCase("helmet")) {
                    // set the helmet
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item.getType() != Material.PLAYER_HEAD) {
                        Lang.send(sender, Lang.NOT_A_HELMET);
                        return true;
                    }
                    Config.saveHelmet(plugin, item);
                    Config.reload(plugin);
                    Lang.send(sender, Lang.SAVED_HELMET);
                    return true;
                }

                if (args[1].equalsIgnoreCase("rocket")) {
                    // set rocket
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item.getType() != Material.FIREWORK_ROCKET) {
                        Lang.send(sender, Lang.NOT_A_ROCKET);
                        return true;
                    }
                    Config.saveRocket(plugin, item);
                    Config.reload(plugin);
                    Lang.send(sender, Lang.SAVED_ROCKET);
                    return true;
                }
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    Lang.send(sender, Lang.PLAYER_NOT_FOUND);
                    return true;
                }

                if (args[1].equalsIgnoreCase("helmet")) {
                    // give target helmet
                    Lang.send(sender, Lang.GAVE_HELMET
                            .replace("{target}", target.getName()));
                    Lang.send(target, Lang.RECEIVED_HELMET);
                    target.getInventory().addItem(Config.HELMET).forEach((Integer, overflow) ->
                            target.getWorld().dropItem(target.getLocation(), overflow)
                                    .setOwner(target.getUniqueId()));
                    return true;
                }

                if (args[1].equalsIgnoreCase("rocket")) {
                    // give target rocket
                    Lang.send(sender, Lang.GAVE_ROCKET
                            .replace("{target}", target.getName()));
                    Lang.send(target, Lang.RECEIVED_ROCKET);
                    target.getInventory().addItem(Config.ROCKET).forEach((Integer, overflow) ->
                            target.getWorld().dropItem(target.getLocation(), overflow)
                                    .setOwner(target.getUniqueId()));
                    return true;
                }
            }
        }

        return false; // show usage
    }
}
