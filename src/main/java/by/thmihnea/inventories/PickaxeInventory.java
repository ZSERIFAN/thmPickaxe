package by.thmihnea.inventories;

import by.thmihnea.PrisonCore;
import by.thmihnea.enchants.UpgradeEnchant;
import by.thmihnea.mysql.MySQLMethods;
import by.thmihnea.utils.ChatColorTranslator;
import by.thmihnea.utils.EnchantUtil;
import by.thmihnea.utils.GiveCrystal;
import de.tr7zw.nbtapi.NBTItem;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;

public class PickaxeInventory implements InventoryProvider {

    MySQLMethods sql = new MySQLMethods();

    public static final SmartInventory pickaxeInventory = SmartInventory.builder()
            .id("pickaxeInventory")
            .provider(new PickaxeInventory())
            .size(6, 9)
            .title(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory_title")))
            .build();

    @Override
    public void init(final Player player, InventoryContents contents) {
        int tokens = 0;
        try {
            tokens = sql.getAvailableTokens(player.getUniqueId());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        final int finalTokens = tokens;
        ItemStack fillable = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);

        ItemMeta fillableMeta = fillable.getItemMeta();
        fillableMeta.setDisplayName(ChatColorTranslator.color(" "));
        fillable.setItemMeta(fillableMeta);
        contents.fillBorders(ClickableItem.empty(fillable));

        ItemStack itemInHand = player.getItemInHand();
        contents.set(1, 4, ClickableItem.empty(itemInHand));

        ItemStack xpBottle = new ItemStack(Material.EXP_BOTTLE, 1);

        ItemMeta xpMeta = xpBottle.getItemMeta();
        String xpTitle = PrisonCore.cfg.getString("pickaxeconfig.inventory.exp_name");
        xpTitle = xpTitle.replace("%amount%", String.valueOf(player.getLevel()));
        xpMeta.setDisplayName(ChatColorTranslator.color(xpTitle));
        ArrayList<String> cfgLore = (ArrayList<String>) PrisonCore.cfg.getList("pickaxeconfig.inventory.exp_lore");
        ArrayList<String> finalLore = new ArrayList<String>();
        for (String str : cfgLore) {
            if (str.contains("%amount%")) {
                str = str.replace("%amount%", String.valueOf(player.getLevel()));
            }
            finalLore.add(ChatColorTranslator.color(str));
        }
        xpMeta.setLore(finalLore);
        xpBottle.setItemMeta(xpMeta);

        contents.set(1, 3, ClickableItem.empty(xpBottle));

        ItemStack crystals = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = crystals.getItemMeta();
        NBTItem nbti = new NBTItem(player.getItemInHand());
        String crystalsTitle = PrisonCore.cfg.getString("pickaxeconfig.inventory.crystals.name");
        crystalsTitle = crystalsTitle.replace("%amount%", String.valueOf(nbti.getInteger("available-crystals")));
        meta.setDisplayName(ChatColorTranslator.color(crystalsTitle));
        ArrayList<String> crystalCfgLore = (ArrayList<String>) PrisonCore.cfg.getList("pickaxeconfig.inventory.crystals.lore");
        ArrayList<String> finalCrystalLore = new ArrayList<String>();
        for (String str : crystalCfgLore) {
            finalCrystalLore.add(ChatColorTranslator.color(str));
        }
        meta.setLore(finalCrystalLore);
        crystals.setItemMeta(meta);
        contents.set(1, 5, ClickableItem.from(crystals, e -> {
            openCrystalsMenu(player);
        }));

        if (player.getItemInHand().getEnchantments().entrySet().isEmpty()) {
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory.enchants.no_enchants")));
            ArrayList<String> enchLore = (ArrayList<String>) PrisonCore.cfg.getList("pickaxeconfig.inventory.enchants.no_enchants_lore");
            ArrayList<String> finalEnchLore = new ArrayList<String>();
            for (String str : enchLore) {
                finalEnchLore.add(ChatColorTranslator.color(str));
            }
            bookMeta.setLore(finalEnchLore);
            book.setItemMeta(bookMeta);
            contents.set(3, 4, ClickableItem.empty(book));
        }
        int i = 0;
        for (Enchantment ench : player.getItemInHand().getEnchantments().keySet()) {
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            String displayName = PrisonCore.cfg.getString("pickaxeconfig.inventory.enchants.name");
            displayName = displayName.replace("%enchant%", EnchantUtil.enchantmentName(ench.getName()));
            displayName = displayName.replace("%ench_power%", String.valueOf(player.getItemInHand().getEnchantments().get(ench)));
            displayName = ChatColorTranslator.color(displayName);
            bookMeta.setDisplayName(displayName);
            ArrayList<String> enchLore = (ArrayList<String>) PrisonCore.cfg.getList("pickaxeconfig.inventory.enchants.lore");
            ArrayList<String> finalEnchLore = new ArrayList<String>();
            for (String str : enchLore) {
                if (str.contains("%enchant%"))
                    str = str.replace("%enchant%", String.valueOf(player.getItemInHand().getEnchantments().get(ench)));
                finalEnchLore.add(ChatColorTranslator.color(str));
            }
            ArrayList<String> enchUpgradeLore = (ArrayList<String>) PrisonCore.cfg.getList("pickaxeconfig.inventory.enchants.can_upgrade_lore");
            if (finalTokens != 0) {
                for (String str : enchUpgradeLore) {
                    if (str.contains("%amount%"))
                        str = str.replace("%amount%", String.valueOf(finalTokens));
                    finalEnchLore.add(ChatColorTranslator.color(str));
                }
            }
            String s = "enchants.maxlevel." + EnchantUtil.enchantmentName(ench.getName()).toLowerCase();
            if (s.equalsIgnoreCase("enchants.maxlevel.token greed")) s = "enchants.maxlevel.tokengreed";
            int maxlevel = PrisonCore.cfg.getInt(s);
            if (player.getItemInHand().getEnchantments().get(ench) >= maxlevel) {
                finalEnchLore.add(ChatColorTranslator.color("&cThis enchantment is maxed out!"));
                finalEnchLore.add(ChatColorTranslator.color("&7&o(You aren't able to upgrade it!)"));
                finalEnchLore.add(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.lore_spacer")));
            }
            bookMeta.setLore(finalEnchLore);
            book.setItemMeta(bookMeta);
            UpgradeEnchant ue = new UpgradeEnchant();
            contents.set(i / 5 + 2, i % 5 + 2, ClickableItem.of(book, e -> {
                try {
                    ue.upgradeEnchant(player, ench);
                    pickaxeInventory.open(player);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }));
            i++;
        }
    }

    public void openCrystalsMenu(Player player) {
        GiveCrystal gc = new GiveCrystal();
        Inventory crystalMenu = Bukkit.createInventory(null, 27, ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory.crystals.title")));
        NBTItem nbti = new NBTItem(player.getItemInHand());
        int t = nbti.getInteger("level");
        int slots = 0;
        if (t >= 50) slots = 1 + t / 250;
        nbti.setInteger("available-crystals", slots);
        for (int i = 1; i <= nbti.getInteger("available-crystals"); i++) {
            if (nbti.getString("crystal-slot" + i + "-name").equalsIgnoreCase("none") || (!(nbti.hasKey("crystal-slot" + i + "-name")))) {
                continue;
            }
            crystalMenu.setItem(i + 8, gc.createCrystal(player, nbti.getString("crystal-slot" + i + "-name"), nbti.getInteger("crystal-slot" + i + "-percentage")));
        }
        for (int i = 0; i <= 8; i++) {
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
            crystalMenu.setItem(i, item);
        }
        for (int i = 18; i <= 26; i++) {
            ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
            crystalMenu.setItem(i, item);
        }
        for (int i = 9 + slots; i <= 17; i++) {
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta meta = barrier.getItemMeta();
            meta.setDisplayName(ChatColorTranslator.color(PrisonCore.cfg.getString("pickaxeconfig.inventory.crystals.slot_locked")));
            barrier.setItemMeta(meta);
            crystalMenu.setItem(i, barrier);
        }
        player.openInventory(crystalMenu);
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
