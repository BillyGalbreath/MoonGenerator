package net.pl3x.bukkit.moongenerator.listener;

import net.pl3x.bukkit.moongenerator.configuration.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class HelmetListener implements Listener {
    private static final int HELMET_SLOT_ID = 5;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!Config.USE_GLASS_HELMETS) {
            return; // glass helmets disabled
        }

        if (!event.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
            return; // not an armor slot
        }

        if (event.getRawSlot() != HELMET_SLOT_ID) {
            return; // not the helmet slot
        }

        ItemStack newHelmet = event.getCursor();
        if (!Config.isGlassHelmet(newHelmet)) {
            return; // not glass helmet
        }

        ItemStack oldHelmet = event.getCurrentItem();

        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Player) {
            // swap items (slot <-> cursor)
            ((Player) holder).getInventory().setHelmet(newHelmet);
            event.getWhoClicked().setItemOnCursor(oldHelmet);
            event.setCancelled(true);
        }
    }
}
