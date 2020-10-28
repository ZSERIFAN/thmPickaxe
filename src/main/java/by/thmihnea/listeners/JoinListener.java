package by.thmihnea.listeners;

import by.thmihnea.PrisonCore;
import by.thmihnea.mysql.MySQLMethods;
import org.apache.commons.lang.time.StopWatch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import ö$.e;

public class JoinListener implements Listener {

    MySQLMethods sql = new MySQLMethods();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!(sql.playerExists(p.getUniqueId()))) {
            System.out.println("[Prison Core] - Found player " + p.getName() + " (UUID: " + p.getUniqueId() + ") that has no records in database. Generating player...");
            StopWatch sw = new StopWatch();
            sw.start();
            sql.createPlayer(p, p.getUniqueId());
            sw.stop();
            System.out.println("[Prison Core] - Successfully added player " + p.getName() + " in database. Process took: " + sw.getTime() + "ms");
        }

    }
}
