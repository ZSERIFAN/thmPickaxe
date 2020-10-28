package by.thmihnea.listeners;

import by.thmihnea.inventories.PickaxeInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        PickaxeInventory pi = new PickaxeInventory();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack i = p.getItemInHand();
            if (!(i.getType() == Material.DIAMOND_PICKAXE
                    || i.getType() == Material.IRON_PICKAXE
                    || i.getType() == Material.GOLD_PICKAXE
                    || i.getType() == Material.STONE_PICKAXE
                    || i.getType() == Material.WOOD_PICKAXE))
                return;
            PickaxeInventory.pickaxeInventory.open(p);
        }
    }
}
