package by.thmihnea.listeners;

import by.thmihnea.PrisonCore;
import by.thmihnea.enchants.EnchantmentWrapper;
import by.thmihnea.utils.ChatColorTranslator;
import by.thmihnea.utils.EnchantUtil;
import com.connorlinfoot.bountifulapi.BountifulAPI;
import de.tr7zw.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.ArrayList;

public class BlockBreakListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        Player p = e.getPlayer();

        if (p.getInventory().getItemInHand().getType() == Material.AIR) return;
        NBTItem nbti = new NBTItem(p.getItemInHand());
        ItemStack i = nbti.getItem();

        if (!(i.getType() == Material.DIAMOND_PICKAXE
                || i.getType() == Material.IRON_PICKAXE
                || i.getType() == Material.GOLD_PICKAXE
                || i.getType() == Material.STONE_PICKAXE
                || i.getType() == Material.WOOD_PICKAXE))
            return;

        if (!(nbti.hasKey("tags"))) {
            for (int j = 1; j <= 9; j++) {
                nbti.setString("crystal-slot" + j + "-name", "none");
                nbti.setInteger("crystal-slot" + j + "-percentage", 0);
            }
            nbti.setInteger("available-crystals", 2);
            nbti.setBoolean("tags", true);
            nbti.setInteger("exp", 0);
            nbti.setInteger("xptonextlevel", PrisonCore.cfg.getInt("pickaxeconfig.default_exp_to_next_level"));
            nbti.setInteger("level", 250);
            nbti.setInteger("crystals", 0);
        }

        int exp = 1;

        if (e.getBlock().getType() == Material.STONE) exp = PrisonCore.cfg.getInt("pickaxeconfig.exp.stone");
        else if (e.getBlock().getType() == Material.COBBLESTONE) exp = PrisonCore.cfg.getInt("pickaxeconfig.exp.cobblestone");
        else if (e.getBlock().getType() == Material.COAL_ORE) exp = PrisonCore.cfg.getInt("pickaxeconfig.exp.coal");
        else if (e.getBlock().getType() == Material.GOLD_ORE) exp = PrisonCore.cfg.getInt("pickaxeconfig.exp.gold");
        else if (e.getBlock().getType() == Material.IRON_ORE) exp = PrisonCore.cfg.getInt("pickaxeconfig.exp.iron");
        else if (e.getBlock().getType() == Material.EMERALD_ORE) exp = PrisonCore.cfg.getInt("pickaxeconfig.exp.emerald");
        else if (e.getBlock().getType() == Material.OBSIDIAN) exp = PrisonCore.cfg.getInt("pickaxeconfig.exp.obsidian");
        else if (e.getBlock().getType() == Material.DIAMOND_ORE) exp = PrisonCore.cfg.getInt("pickaxeconfig.exp.diamond");
        else if (e.getBlock().getType() == Material.REDSTONE_ORE) exp = PrisonCore.cfg.getInt("pickaxeconfig.exp.redstone");
        else exp = PrisonCore.cfg.getInt("pickaxeconfig.exp.else");

        if (p.getItemInHand().getEnchantments().keySet().contains(EnchantmentWrapper.EXPERIENCED)) {
            exp += 0.1 * (double) p.getItemInHand().getEnchantmentLevel(EnchantmentWrapper.EXPERIENCED) * exp;
        }

        int xpnl = nbti.getInteger("level") * Integer.parseInt(PrisonCore.cfg.getString("pickaxeconfig.default_exp_increment"));
        if (p.getItemInHand().getEnchantments().keySet().contains(EnchantmentWrapper.LAZY)) {
            xpnl -= 0.02 * (double) p.getItemInHand().getEnchantmentLevel(EnchantmentWrapper.LAZY) * xpnl;
        }

        if (nbti.getInteger("level") == 2000) nbti.setBoolean("isMaxLevel", true);
        else nbti.setBoolean("isMaxLevel", false);

        if (((nbti.getInteger("exp") + 1) >= nbti.getInteger("xptonextlevel")) && (nbti.getInteger("level") != 2000)) {
            nbti.setInteger("exp", 0);
            nbti.setInteger("level", nbti.getInteger("level") + 1);
            nbti.setInteger("xptonextlevel", xpnl);
            if (nbti.getInteger("level") >= 50) {
                nbti.setInteger("available-crystals", 1 + nbti.getInteger("level")/250);
            }
        } else {
            if (!(nbti.getBoolean("isMaxLevel"))) {
                nbti.setInteger("exp", nbti.getInteger("exp") + exp);
            }
        }

        String message = ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.actionbar_message"));
        message = message.replace("%amount%", String.format("%,d", exp));
        message = message.replace("%xp%", String.format("%,d", nbti.getInteger("exp")));
        message = message.replace("%xptonextlevel%", String.format("%,d", nbti.getInteger("xptonextlevel")));

        BountifulAPI.sendActionBar(p, message);

        i = nbti.getItem();

        ItemMeta meta = i.getItemMeta();
        ArrayList<String> lore = (ArrayList<String>) meta.getLore();
        ArrayList<String> newLore = new ArrayList<String>();

        ArrayList<String> pickLevelLore = new ArrayList<String>();
        ArrayList<String> crystalsLore = new ArrayList<>();
        ArrayList<String> enchantmentLore = new ArrayList<>();

        boolean foundlvl = false;
        boolean foundexp = false;

        pickLevelLore.add(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.lore_spacer")));
        if (lore != null) {
            for (String str : lore) {
                if (str.contains(PrisonCore.cfg.getString("pickaxeconfig.level_identifier"))) {
                    String newStr = "";
                    if (nbti.getBoolean("isMaxLevel"))
                        newStr = ChatColorTranslator.color(str.substring(0, str.indexOf(":") + 1) + " &e" + String.format("%,d", nbti.getInteger("level")) + " " + PrisonCore.cfg.getString("pickaxeconfig.max_level"));
                    else
                        newStr = ChatColorTranslator.color(str.substring(0, str.indexOf(":") + 1) + " &e" + String.format("%,d", nbti.getInteger("level")));
                    pickLevelLore.add(newStr);
                    foundlvl = true;
                }
                else if (str.contains(PrisonCore.cfg.getString("pickaxeconfig.exp_identifier"))) {
                    String newStr = "";
                    if (nbti.getBoolean("isMaxLevel"))
                        newStr = ChatColorTranslator.color(str.substring(0, str.indexOf(":") + 1) + " 5,017,500/5,017,500 " + PrisonCore.cfg.getString("pickaxeconfig.max_level"));
                    else
                        newStr = ChatColorTranslator.color(str.substring(0, str.indexOf(":") + 1) + " &e" + String.format("%,d", nbti.getInteger("exp")) + "&8/&e" + String.format("%,d", nbti.getInteger("xptonextlevel")));
                    pickLevelLore.add(newStr);
                    foundexp = true;
                }
            }
            if (!foundlvl) pickLevelLore.add(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.level_format") + String.format("%,d", nbti.getInteger("level"))));
            if (!foundexp) pickLevelLore.add(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.exp_format") + String.format("%,d", nbti.getInteger("exp")) + "&8/&e" + String.format("%,d", nbti.getInteger("xptonextlevel"))));
        }
        else {
            pickLevelLore.add(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.level_format") + nbti.getInteger("level")));
            pickLevelLore.add(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.exp_format") + nbti.getInteger("exp") + "&8/&e" + nbti.getInteger("xptonextlevel")));
        }
        pickLevelLore.add(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.lore_spacer")));
        if (nbti.getInteger("available-crystals") == 0) {
            crystalsLore.add(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.locked_slots")));
        }
        else if (nbti.getInteger("available-crystals") >= 1) {
            for (int k = 1; k <= nbti.getInteger("available-crystals"); k++) {
                if (nbti.getString("crystal-slot" + k + "-name").equalsIgnoreCase("none")) {
                    String s = ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.empty_crystal_slot"));
                    crystalsLore.add(s);
                } else {
                    String s = ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.crystal_slot"));
                    s = s.replace("%percentage%", String.valueOf(nbti.getInteger("crystal-slot" + k + "-percentage")));
                    String type = nbti.getString("crystal-slot" + k + "-name");
                    s = s.replace("%type%", type.substring(0, 1).toUpperCase() + type.substring(1) + " Boost");
                    crystalsLore.add(s);
                }
            }
        }
        crystalsLore.add(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.lore_spacer")));
        if (!(p.getItemInHand().getEnchantments().entrySet().isEmpty())) {
            for (Enchantment ench : p.getItemInHand().getEnchantments().keySet()) {
                String s = ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.enchantment_lore_line"));
                s = s.replace("%enchantment%", EnchantUtil.enchantmentName(ench.getName()));
                s = s.replace("%power%", String.valueOf(p.getItemInHand().getEnchantments().get(ench)));
                enchantmentLore.add(s);
            }
            enchantmentLore.add(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.lore_spacer")));
        }

        newLore.addAll(pickLevelLore);
        newLore.addAll(enchantmentLore);
        newLore.addAll(crystalsLore);

        meta.setLore(newLore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        p.getInventory().getItemInHand().setItemMeta(meta);
    }
}
