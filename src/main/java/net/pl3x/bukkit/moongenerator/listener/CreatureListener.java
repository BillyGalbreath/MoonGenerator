package net.pl3x.bukkit.moongenerator.listener;

import net.pl3x.bukkit.moongenerator.configuration.Config;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CreatureListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onMobSpawn(CreatureSpawnEvent event) {
        if (!Config.isMoon(event.getEntity().getWorld())) {
            return; // not a moon creature
        }

        LivingEntity mob = event.getEntity();
        if (mob.getType().equals(EntityType.CREEPER)
                || mob.getType().equals(EntityType.ZOMBIE)
                || mob.getType().equals(EntityType.HUSK)
                || mob.getType().equals(EntityType.SKELETON)
                || mob.getType().equals(EntityType.STRAY)
                || mob.getType().equals(EntityType.ZOMBIE_HORSE)
                || mob.getType().equals(EntityType.SKELETON_HORSE)
                || mob.getType().equals(EntityType.ZOMBIE_VILLAGER)
                || mob.getType().equals(EntityType.PIG_ZOMBIE)) {
            // we only want these mobs on the moon
        } else {
            // everything else can stay on earth
            event.setCancelled(true);
            return;
        }

        // moon creatures are affected by moon gravity
        mob.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 3), true);
        mob.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 3), true);

        // moon creatures where a helmet (if possible)
        EntityEquipment equipment = mob.getEquipment();
        if (equipment != null) {
            equipment.setHelmet(new ItemStack(Material.GLASS));
        }
    }
}
