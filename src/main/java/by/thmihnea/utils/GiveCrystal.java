package by.thmihnea.utils;

import by.thmihnea.PrisonCore;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;

public class GiveCrystal {

    public ItemStack createCrystal(Player player, String type, int boost) {
        Random rand = new Random();
        int randint = rand.nextInt(100000000);
        NBTItem nbti = new NBTItem(new ItemStack(Material.NETHER_STAR));
        nbti.setInteger("percentage", boost);
        if (type.equalsIgnoreCase("sell")) nbti.setString("type", "sell");
        else if (type.equalsIgnoreCase("token")) nbti.setString("type", "token");
        else if (type.equalsIgnoreCase("exp")) nbti.setString("type", "exp");
        ItemStack item = nbti.getItem();
        ItemMeta meta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        String rarity = "";
        if (boost <= 75) rarity = ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory.crystals.rarity.common"));
        else if (boost > 75 && boost <= 250) rarity = ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory.crystals.rarity.uncommon"));
        else if (boost > 250 && boost <= 500) rarity = ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory.crystals.rarity.rare"));
        lore.add(" ");
        String boostType = "";
        if (type.equalsIgnoreCase("sell")) {
            boostType = ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory.crystals.type.sell_boost"));
        }
        else if (type.equalsIgnoreCase("token")) {
            boostType = ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory.crystals.type.token_boost"));
        }
        else if (type.equalsIgnoreCase("exp")) {
            boostType = ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory.crystals.type.exp_boost"));
        }
        lore.add(ChatColorTranslator.color("&eBoost Type: " + boostType));
        lore.add(ChatColorTranslator.color("&eBoost Percentage: " + boost + "%"));
        lore.add(HiddenStringUtils.encodeString(String.valueOf(randint)));
        lore.add(ChatColorTranslator.color(rarity + " CRYSTAL"));
        meta.setLore(lore);
        meta.setDisplayName(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory.crystals.crystal_name")));
        item.setItemMeta(meta);
        return item;
    }
}
