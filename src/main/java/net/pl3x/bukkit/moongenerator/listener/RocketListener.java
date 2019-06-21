package net.pl3x.bukkit.moongenerator.listener;

import net.pl3x.bukkit.moongenerator.MoonGenerator;
import net.pl3x.bukkit.moongenerator.configuration.Config;
import net.pl3x.bukkit.moongenerator.configuration.Lang;
import net.pl3x.bukkit.moongenerator.task.RocketTask;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.FireworkMeta;

public class RocketListener implements Listener {
    private final MoonGenerator plugin;

    public RocketListener(MoonGenerator plugin) {
        this.plugin = plugin;
    }

    // Use rocket logic
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!Config.USE_ROCKETS) {
            return; // rockets disabled
        }

        if (event.getHand() != EquipmentSlot.HAND) {
            return; // do not fire twice
        }

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return; // not right clicking a block
        }

        Player player = event.getPlayer();
        if (!(Config.isEarth(player.getWorld()) || Config.isMoon(player.getWorld()))) {
            return; // not on earth or moon world
        }

        if (player.isSneaking()) {
            return; // is sneaking
        }

        if (!Config.isRocket(player.getInventory().getItemInMainHand())) {
            return; // not holing a valid rocket
        }

        if (!player.hasPermission("moongenerator.userocket")) {
            return; // no permission to use rockets
        }

        Lang.sendActionBar(player, Lang.ROCKET_COUNTDOWN);
        event.setCancelled(true);

        Firework rocket = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta rocketMeta = rocket.getFireworkMeta();
        rocketMeta.setPower(127);
        rocket.setFireworkMeta(rocketMeta);

        rocket.addPassenger(player);

        new RocketTask(plugin, player, rocket).runTaskTimer(plugin, 20L, 10L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getEntity().hasMetadata("no-fall-damage")) {
            event.getEntity().removeMetadata("no-fall-damage", plugin);
            event.setCancelled(true);
        }
    }
}
