package net.pl3x.bukkit.moongenerator.listener;

import net.pl3x.bukkit.moongenerator.configuration.Config;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

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

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryShiftClick(InventoryClickEvent event) {
        if (event.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            return; // not moving item around by shift clicking
        }

        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            return; // don't care if shift clicking out of the armor slot
        }

        ItemStack newHelmet = event.getCurrentItem();
        if (!Config.isGlassHelmet(newHelmet)) {
            return; // not glass helmet
        }

        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Player) {
            PlayerInventory pInv = ((Player) holder).getInventory();

            if (pInv.getHelmet() != null) {
                return; // already wearing a helmet
            }

            pInv.setHelmet(newHelmet.clone());
            pInv.setItem(event.getSlot(), null);

            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() != Material.PLAYER_HEAD) {
            return;
        }
        Skull skull = (Skull) block.getState();
        OfflinePlayer owner = skull.getOwningPlayer();
        if (owner == null) {
            return; // no owner set
        }
        if (!owner.equals(((SkullMeta) Config.HELMET.getItemMeta()).getOwningPlayer())) {
            return; // not our moon helmet
        }
        event.setDropItems(false);
        block.getWorld().dropItemNaturally(block.getLocation(), Config.HELMET);
    }
}
