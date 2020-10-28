package by.thmihnea;

import by.thmihnea.commands.CreateCrystalCommand;
import by.thmihnea.commands.SellCommand;
import by.thmihnea.enchants.EnchantmentWrapper;
import by.thmihnea.listeners.*;
import by.thmihnea.mysql.MySQLConnection;
import by.thmihnea.utils.ChatColorTranslator;
import by.thmihnea.utils.EnchantUtil;
import by.thmihnea.utils.UpdateLore;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.time.StopWatch;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PrisonCore extends JavaPlugin {

    private static PrisonCore instance;
    private Player player;

    public static PrisonCore getInstance() {
        return instance;
    }

    public static File config = new File("plugins/PrisonCore/config.yml");

    public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(config);

    public static File playerData = new File("plugins/PrisonCore/data");

    @Override
    public void onEnable() {
        System.out.println("[Prison Core] - Attempting to enable plugin modules...");

        StopWatch sw = new StopWatch();
        sw.start();

        instance = this;

        System.out.println("[Prison Core] - Attempting to enable configuration files.");
        File file1 = new File("plugins", "PrisonCore");
        if (!file1.exists()) file1.mkdir();
        if (!config.exists()) saveDefaultConfig();
        try {
            cfg.load(config);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        if (!playerData.exists()) playerData.mkdir();
        System.out.println("[Prison Core] - Loaded configuration module. (" + sw.getTime() + "ms)");

        System.out.println("[Prison Core] - Attempting to enable SQL module.");

        MySQLConnection sql = new MySQLConnection();
        sql.sqlConnect();
        sql.createTable();

        System.out.println("[Prison Core] - Loaded SQL module. (" + sw.getTime() + "ms)");

        System.out.println("[Prison Core] - Attempting to enable listeners.");

        registerEvents();

        System.out.println("[Prison Core] - Successfully enabled listeners. (" + sw.getTime() + "ms)");

        System.out.println("[Prison Core] - Attempting to enable commands.");

        new SellCommand(this);
        new CreateCrystalCommand(this);

        System.out.println("[Prison Core] - Successfully enabled commands. (" + sw.getTime() + "ms)");

        sw.stop();
        System.out.println("[Prison Core] - Successfully enabled all modules. Process took: " + sw.getTime() + "ms");

        EnchantmentWrapper.register();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getItemInHand().getEnchantments().keySet().contains(EnchantmentWrapper.SPEED)) {
                        p.setWalkSpeed((float) (0.2 + ((float) p.getItemInHand().getEnchantmentLevel(EnchantmentWrapper.SPEED) * 0.05)));
                    } else {
                        p.setWalkSpeed(0.2F);
                    }
                }
            }
        }, 0L, 5L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
            UpdateLore ul = new UpdateLore();
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers())
                    ul.updateLore(p);
            }
        }, 0L, 25L);
    }

    @Override
    public void onDisable() {
        System.out.println("[Prison Core] - Disabling modules.");
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new JoinListener(), instance);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), instance);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), instance);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), instance);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), instance);
        Bukkit.getPluginManager().registerEvents(new SellCrystal(), instance);
        Bukkit.getPluginManager().registerEvents(new EXPCrystal(), instance);
        Bukkit.getPluginManager().registerEvents(new LuckyEnchant(), instance);
        Bukkit.getPluginManager().registerEvents(new FortuneEnchant(), instance);
    }
}
