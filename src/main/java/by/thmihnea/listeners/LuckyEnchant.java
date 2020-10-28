package by.thmihnea.listeners;

import by.thmihnea.enchants.EnchantmentWrapper;
import by.thmihnea.mysql.MySQLMethods;
import by.thmihnea.utils.ChatColorTranslator;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;

public class LuckyEnchant implements Listener {

    MySQLMethods sql = new MySQLMethods();

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if (e.isCancelled()) return;

        if (p.getItemInHand().getType() == Material.AIR) return;

        if (p.getItemInHand().getType().toString().toLowerCase().contains("pickaxe")) return;
        NBTItem nbti = new NBTItem(p.getItemInHand());

        double chance = 0.5;
        int t = 0;
        Random rand = new Random();
        double hash = 100 * rand.nextDouble();
        int tokens = rand.nextInt(20);
        if (tokens == 0) tokens = 1;

        if (p.getItemInHand().getEnchantments().keySet().contains(EnchantmentWrapper.LUCKY)) {
            chance += ((double) p.getItemInHand().getEnchantments().get(EnchantmentWrapper.LUCKY) * 0.4);
        }
        if (p.getItemInHand().getEnchantments().keySet().contains(EnchantmentWrapper.TOKENGREED)) {
            t = (int) (((double) p.getItemInHand().getEnchantments().get(EnchantmentWrapper.TOKENGREED) * 0.11) * tokens);
            tokens += (((double) p.getItemInHand().getEnchantments().get(EnchantmentWrapper.TOKENGREED) * 0.11) * tokens);
        }
        double crystalBoost = 100;
        for (int i = 1; i <= nbti.getInteger("available-crystals"); i++) {
            if (nbti.getString("crystal-slot" + i + "-name").equalsIgnoreCase("token"))
                crystalBoost += nbti.getInteger("crystal-slot" + i + "-percentage");
        }
        double boost = crystalBoost/100;
        double finalTokens = (double) tokens * boost;

        if (hash < chance) {
            sql.giveTokenXp(p, (int) finalTokens);
            if (chance != 0.5) p.sendMessage(ChatColorTranslator.color("&6Lucky &eincreased your chance of receiving tokens by &6" + (int) (chance - 0.5) + "%"));
            if (t != 0) p.sendMessage(ChatColorTranslator.color("&6Token Greed &egranted you &6" + t + " &emore tokens."));
            if (boost != 1) p.sendMessage(ChatColorTranslator.color("&6Crystals &egranted you &6" + boost + "x &emore tokens."));
        }
    }
}
