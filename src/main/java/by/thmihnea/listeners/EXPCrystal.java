package by.thmihnea.listeners;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class EXPCrystal implements Listener {

    @EventHandler
    public void onXPChange(PlayerExpChangeEvent e) {
        int amount = e.getAmount();
        double t = amount;
        double multiplier = 100;
        NBTItem nbti = new NBTItem(e.getPlayer().getItemInHand());
        for (int i = 1; i <= nbti.getInteger("available-crystals"); i++) {
            if (nbti.getString("crystal-slot" + i + "-name").equalsIgnoreCase("exp"))
                multiplier += nbti.getInteger("crystal-slot" + i + "-percentage");
        }
        multiplier /= 100;
        e.setAmount((int) (t * multiplier));
    }
}
