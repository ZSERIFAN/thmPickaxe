package by.thmihnea.listeners;

import by.thmihnea.PrisonCore;
import by.thmihnea.utils.ChatColorTranslator;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        int cnt = 0;
        if (p.getItemInHand().getType() == Material.AIR || p.getItemInHand() == null) return;
        NBTItem pick = new NBTItem(p.getItemInHand());
        if (e.getInventory().getTitle().equalsIgnoreCase(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory.crystals.title")))) {
            for (int i = 9; i <= 17; i++) {
                Inventory inv = e.getInventory();
                if (inv.getItem(i) == null) {
                    pick.setString("crystal-slot" + (i-8) + "-name", "none");
                    pick.setInteger("crystal-slot" + (i-8) + "-percentage", 0);
                    continue;
                } else if (inv.getItem(i) != null){
                    NBTItem nbtinv = new NBTItem(inv.getItem(i));
                    if (!(nbtinv.hasKey("type"))) continue;
                    pick.setString("crystal-slot" + (i - 8) + "-name", nbtinv.getString("type"));
                    pick.setInteger("crystal-slot" + (i - 8) + "-percentage", nbtinv.getInteger("percentage"));
                    cnt++;
                }
            }
        }
        pick.setInteger("crystals", cnt);
        ItemStack item = pick.getItem();
        p.getInventory().getItemInHand().setItemMeta(item.getItemMeta());
    }
}
