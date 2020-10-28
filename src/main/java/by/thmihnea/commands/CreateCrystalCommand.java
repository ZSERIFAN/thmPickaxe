package by.thmihnea.commands;

import by.thmihnea.PrisonCore;
import by.thmihnea.utils.ChatColorTranslator;
import by.thmihnea.utils.GiveCrystal;
import net.md_5.bungee.protocol.packet.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCrystalCommand implements CommandExecutor {

    private PrisonCore plugin = PrisonCore.getInstance();

    public CreateCrystalCommand(PrisonCore plugin) {
        this.plugin = plugin;

        plugin.getCommand("createcrystal").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (!(p.hasPermission("*"))) {
            p.sendMessage(ChatColorTranslator.color("&cYou don't have permission to use this command!"));
            return true;
        }
        if (args.length < 2) {
            p.sendMessage(ChatColorTranslator.color("&cInsufficient arguments. Specified: " + args.length + " (Needed: 2)!"));
            return true;
        }
        if (!(args[0].equalsIgnoreCase("sell") || args[0].equalsIgnoreCase("token") || args[0].equalsIgnoreCase("exp"))) {
            p.sendMessage(ChatColorTranslator.color("&cCrystal type " + args[0] + " doesn't exist. (Available: SELL, TOKEN, EXP)"));
            return true;
        }
        if (!(Integer.parseInt(args[1]) < 500)) {
            p.sendMessage(ChatColorTranslator.color("&cNumber must be between 0-500! (Your input: " + args[1] + ")"));
            return true;
        }
        GiveCrystal gc = new GiveCrystal();
        p.getInventory().addItem(gc.createCrystal(p, args[0], Integer.parseInt(args[1])));
        p.sendMessage(ChatColorTranslator.color("&eSuccessfully created &6Crystal&e! (Type: &6" + args[0].toUpperCase() + " &8-&e Boost Amount: &6" + args[1] + "%)"));
        return false;
    }
}
