package by.thmihnea.commands;

import by.thmihnea.PrisonCore;
import by.thmihnea.enchants.EnchantmentWrapper;
import by.thmihnea.utils.ChatColorTranslator;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SellCommand implements CommandExecutor {

    private PrisonCore plugin = PrisonCore.getInstance();

    public SellCommand(PrisonCore plugin) {
        this.plugin = plugin;

        plugin.getCommand("sellall").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if (p.getItemInHand().getType() == Material.AIR || p.getItemInHand() == null) {
            p.sendMessage(ChatColorTranslator.color("&c&l(!) &cSell the items while holding a pickaxe in your hand!"));
            return true;
        }

        if (!(p.getItemInHand().getType().toString().toLowerCase().contains("pickaxe"))) {
            p.sendMessage(ChatColorTranslator.color("&c&l(!) &cSell the items while holding a pickaxe in your hand!"));
            return true;
        }

        int price = 0;
        int cnt = 0;
        int multiplier = 100;
        for (int j = 0; j < 36; j++) {
            int s = 0;
            ItemStack i = p.getInventory().getItem(j);
            if (i == null) continue;
            if (i.getType() == Material.DIAMOND_PICKAXE ||
                    i.getType() == Material.IRON_PICKAXE ||
                    i.getType()== Material.GOLD_PICKAXE ||
                    i.getType() == Material.STONE_PICKAXE ||
                    i.getType()== Material.WOOD_PICKAXE) continue;
            if (i.getType() == Material.NETHER_STAR) continue;
            if (i.getType() == Material.EMERALD)
                s += (PrisonCore.cfg.getInt("autosell.prices.emerald") * i.getAmount());
            else if (i.getType() == Material.DIAMOND)
                s += PrisonCore.cfg.getInt("autosell.prices.diamond") * i.getAmount();
            else if (i.getType() == Material.GOLD_INGOT)
                s += PrisonCore.cfg.getInt("autosell.prices.gold") * i.getAmount();
            else if (i.getType() == Material.IRON_INGOT)
                s += PrisonCore.cfg.getInt("autosell.prices.iron") * i.getAmount();
            else if (i.getType() == Material.REDSTONE)
                s += PrisonCore.cfg.getInt("autosell.prices.redstone") * i.getAmount();
            else if (i.getType() == Material.LAPIS_ORE)
                s += PrisonCore.cfg.getInt("autosell.prices.lapis") * i.getAmount();
            else if (i.getType() == Material.QUARTZ)
                s += PrisonCore.cfg.getInt("autosell.prices.quartz") * i.getAmount();
            else if (i.getType() == Material.COAL)
                s += PrisonCore.cfg.getInt("autosell.prices.coal") * i.getAmount();
            else if (i.getType() == Material.STONE)
                s += PrisonCore.cfg.getInt("autosell.prices.stone") * i.getAmount();
            else if (i.getType() == Material.COBBLESTONE)
                s += PrisonCore.cfg.getInt("autosell.prices.cobblestone") * i.getAmount();
            else if (i.getType() == Material.GLOWSTONE)
                s += PrisonCore.cfg.getInt("autosell.prices.glowstone") * i.getAmount();
            else if (i.getType() == Material.ENDER_STONE)
                s += PrisonCore.cfg.getInt("autosell.prices.endstone") * i.getAmount();
            else if (i.getType() == Material.NETHERRACK)
                s += PrisonCore.cfg.getInt("autosell.prices.netherrack") * i.getAmount();
            else if (i.getType() == Material.DIRT)
                s += PrisonCore.cfg.getInt("autosell.prices.dirt") * i.getAmount();
            else if (i.getType() == Material.SAND)
                s += PrisonCore.cfg.getInt("autosell.prices.sand") * i.getAmount();
            else if (i.getType() == Material.GRAVEL)
                s += PrisonCore.cfg.getInt("autosell.prices.gravel") * i.getAmount();
            else if (i.getType() == Material.WOOD)
                s += PrisonCore.cfg.getInt("autosell.prices.wood") * i.getAmount();
            else if (i.getType() == Material.WOOL)
                s += PrisonCore.cfg.getInt("autosell.prices.wool") * i.getAmount();
            price += s;
            cnt += i.getAmount();
            p.getInventory().setItem(j, new ItemStack(Material.AIR));
        }
        if (price == 0) {
            p.sendMessage(ChatColorTranslator.color("&cYour inventory does not contain any saleable item!"));
            return true;
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
        return false;
    }
}
