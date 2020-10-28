package by.thmihnea.listeners;

import by.thmihnea.PrisonCore;
import by.thmihnea.enchants.EnchantmentWrapper;
import by.thmihnea.utils.ChatColorTranslator;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class SellCrystal implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        int price = 0;
        int cnt = 0;
        int multiplier = 100;
        if (p.getInventory().firstEmpty() == -1) {
            if (!(p.getItemInHand().getType().toString().toLowerCase().contains("pickaxe"))) {
                p.sendMessage(ChatColorTranslator.color("&c&l(!) &cHold a pickaxe in your hand so I can attribute levels to it."));
                return;
            }
            for (int j = 0; j < 36; j++) {
                int s = 0;
                ItemStack i = p.getInventory().getItem(j);
                if (p.getInventory().getItem(j).getType() == Material.DIAMOND_PICKAXE ||
                        p.getInventory().getItem(j).getType() == Material.IRON_PICKAXE ||
                        p.getInventory().getItem(j).getType() == Material.GOLD_PICKAXE ||
                        p.getInventory().getItem(j).getType() == Material.STONE_PICKAXE ||
                        p.getInventory().getItem(j).getType() == Material.WOOD_PICKAXE) continue;
                if (i.getType() == Material.NETHER_STAR) continue;
                if (i.getType() == Material.EMERALD) s += (PrisonCore.cfg.getInt("autosell.prices.emerald") * i.getAmount());
                else if (i.getType() == Material.DIAMOND) s += PrisonCore.cfg.getInt("autosell.prices.diamond") * i.getAmount();
                else if (i.getType() == Material.GOLD_INGOT) s += PrisonCore.cfg.getInt("autosell.prices.gold") * i.getAmount();
                else if (i.getType() == Material.IRON_INGOT) s += PrisonCore.cfg.getInt("autosell.prices.iron") * i.getAmount();
                else if (i.getType() == Material.REDSTONE) s += PrisonCore.cfg.getInt("autosell.prices.redstone") * i.getAmount();
                else if (i.getType() == Material.INK_SACK) s += PrisonCore.cfg.getInt("autosell.prices.lapis") * i.getAmount();
                else if (i.getType() == Material.QUARTZ) s += PrisonCore.cfg.getInt("autosell.prices.quartz") * i.getAmount();
                else if (i.getType() == Material.COAL) s += PrisonCore.cfg.getInt("autosell.prices.coal") * i.getAmount();
                else if (i.getType() == Material.STONE) s += PrisonCore.cfg.getInt("autosell.prices.stone") * i.getAmount();
                else if (i.getType() == Material.COBBLESTONE) s += PrisonCore.cfg.getInt("autosell.prices.cobblestone") * i.getAmount();
                else if (i.getType() == Material.GLOWSTONE) s += PrisonCore.cfg.getInt("autosell.prices.glowstone") * i.getAmount();
                else if (i.getType() == Material.ENDER_STONE) s += PrisonCore.cfg.getInt("autosell.prices.endstone") * i.getAmount();
                else if (i.getType() == Material.NETHERRACK) s += PrisonCore.cfg.getInt("autosell.prices.netherrack") * i.getAmount();
                else if (i.getType() == Material.DIRT) s += PrisonCore.cfg.getInt("autosell.prices.dirt") * i.getAmount();
                else if (i.getType() == Material.SAND) s += PrisonCore.cfg.getInt("autosell.prices.sand") * i.getAmount();
                else if (i.getType() == Material.GRAVEL) s += PrisonCore.cfg.getInt("autosell.prices.gravel") * i.getAmount();
                else if (i.getType() == Material.WOOD) s += PrisonCore.cfg.getInt("autosell.prices.wood") * i.getAmount();
                else if (i.getType() == Material.WOOL) s += PrisonCore.cfg.getInt("autosell.prices.wool") * i.getAmount();
                price += s;
                cnt += i.getAmount();
                p.getInventory().setItem(j, new ItemStack(Material.AIR));
            }
            NBTItem nbti = new NBTItem(p.getItemInHand());
            for (int i = 1; i <= nbti.getInteger("available-crystals"); i++) {
                if (nbti.getString("crystal-slot" + i + "-name").equalsIgnoreCase("sell"))
                    multiplier += nbti.getInteger("crystal-slot" + i + "-percentage");
            }
            if (p.getItemInHand().getEnchantments().keySet().contains(EnchantmentWrapper.GREED))
                multiplier += (double) p.getItemInHand().getEnchantmentLevel(EnchantmentWrapper.GREED) * 0.2;
            double d = (double) multiplier / (double) 100;
            price *= d;
            p.sendMessage(ChatColorTranslator.color("&eYou sold a total of &6" + cnt + " &eitems for &6" + price + "$!"));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco give " + p.getName() + " " + price);
        }
    }
}
