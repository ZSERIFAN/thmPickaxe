package by.thmihnea.enchants;

import by.thmihnea.PrisonCore;
import by.thmihnea.inventories.PickaxeInventory;
import by.thmihnea.mysql.MySQLMethods;
import by.thmihnea.utils.ChatColorTranslator;
import by.thmihnea.utils.EnchantUtil;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class UpgradeEnchant {

    MySQLMethods sql = new MySQLMethods();
    PickaxeInventory pi = new PickaxeInventory();

    public void upgradeEnchant(Player p, Enchantment ench) throws SQLException {

        String s = "enchants.maxlevel." + EnchantUtil.enchantmentName(ench.getName()).toLowerCase();
        if (s.equalsIgnoreCase("enchants.maxlevel.token greed")) s = "enchants.maxlevel.tokengreed";
        int maxlevel = PrisonCore.cfg.getInt(s);
        if (sql.getAvailableTokens(p.getUniqueId()) == 0) {
            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            return;
        }
        int i = p.getItemInHand().getEnchantmentLevel(ench);
        if (i >= maxlevel) {
            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            p.sendMessage(ChatColorTranslator.color("&cYou have already maxed out this Enchantment!"));
            return;
        }
        p.getItemInHand().addUnsafeEnchantment(ench, (i + 1));
        sql.setAvailableTokens(p.getUniqueId(), sql.getAvailableTokens(p.getUniqueId()) - 1);
        p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
    }
}
