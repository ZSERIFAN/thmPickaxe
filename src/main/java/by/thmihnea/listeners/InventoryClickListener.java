package by.thmihnea.listeners;

import by.thmihnea.PrisonCore;
import by.thmihnea.utils.ChatColorTranslator;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if (inv.getTitle().equalsIgnoreCase(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory.crystals.title")))) {
            if (e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE || e.getCurrentItem().getType() == Material.BARRIER) e.setCancelled(true);
            if (e.getCursor().getType() == Material.NETHER_STAR && e.getCurrentItem().getType() == null) e.setCancelled(false);
        }
    }
}
