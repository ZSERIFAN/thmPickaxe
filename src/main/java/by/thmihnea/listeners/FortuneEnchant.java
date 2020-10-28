package by.thmihnea.listeners;

import by.thmihnea.enchants.EnchantmentWrapper;
import by.thmihnea.utils.FortuneUtil;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class FortuneEnchant implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        Player p = e.getPlayer();

        if (p.getItemInHand().getType() == Material.AIR) return;

        if (!(p.getItemInHand().getEnchantments().keySet().contains(EnchantmentWrapper.FORTUNE))) return;

        if (!(e.getBlock().getType() == Material.DIAMOND_ORE ||
                e.getBlock().getType() == Material.IRON_ORE ||
                e.getBlock().getType() == Material.COAL_ORE ||
                e.getBlock().getType() == Material.EMERALD_ORE ||
                e.getBlock().getType() == Material.REDSTONE_ORE ||
                e.getBlock().getType() == Material.LAPIS_ORE)) return;

        Material drop = e.getBlock().getType();
        if (drop == Material.DIAMOND_ORE) drop = Material.DIAMOND;
        else if (drop == Material.IRON_ORE) drop = Material.IRON_INGOT;
        else if (drop == Material.COAL_ORE) drop = Material.COAL;
        else if (drop == Material.EMERALD_ORE) drop = Material.EMERALD;
        else if (drop == Material.REDSTONE_ORE) drop = Material.REDSTONE;
        else if (drop == Material.LAPIS_ORE) drop = Material.INK_SACK;
        int level = p.getItemInHand().getEnchantmentLevel(EnchantmentWrapper.FORTUNE);
        e.getBlock().setType(Material.AIR);
        if (drop == Material.INK_SACK) e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(drop, 2 * FortuneUtil.calc(level), (short) 4));
        else e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(drop, FortuneUtil.calc(level)));
    }
}
