package net.pl3x.bukkit.moongenerator.listener;

import net.pl3x.bukkit.moongenerator.configuration.Config;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
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

        if (event.getRawSlot() == HELMET_SLOT_ID) {
            return; // not the helmet slot
        }

        ItemStack newHelmet = event.getCursor();
        if (newHelmet == null || newHelmet.getType() == Material.AIR) {
            return; // nothing on cursor
        }

        if (!Config.isGlassHelmet(newHelmet)) {
            return; // not glass
        }

        // kill the event
        event.setCancelled(true);

        // swap items (slot <-> cursor)
        ItemStack oldHelmet = event.getCurrentItem();
        event.getInventory().setItem(HELMET_SLOT_ID, newHelmet);
        event.getWhoClicked().setItemOnCursor(oldHelmet);
    }
}
