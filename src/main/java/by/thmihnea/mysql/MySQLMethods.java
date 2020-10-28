package by.thmihnea.mysql;

import by.thmihnea.PrisonCore;
import by.thmihnea.utils.ChatColorTranslator;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;

public class MySQLMethods {

    private MySQLConnection con = new MySQLConnection();

    public boolean playerExists(UUID uuid) {
        try {
            PreparedStatement ps = con.getConnection().prepareStatement("SELECT * FROM player_data WHERE UUID = ?");
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return true;
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createPlayer(Player p, UUID uuid) {
        try {
            PreparedStatement ps = con.getConnection().prepareStatement("SELECT * FROM player_data WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (!(playerExists(uuid))) {
                PreparedStatement ps2 = con.getConnection()
                        .prepareStatement("INSERT INTO `player_data` (UUID, TOKENS, CURRENTXP, XPTONEXTLEVEL, AVAILABLETOKENS) VALUE (?, ?, ?, ?, ?)");
                ps2.setString(1, uuid.toString());
                ps2.setInt(2, 1);
                ps2.setInt(3, 0);
                ps2.setInt(4, 20000);
                ps2.setInt(5, 1);
                ps2.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setTokens(UUID uuid, int tokens) {
        try {
            PreparedStatement ps = con.getConnection()
                    .prepareStatement("UPDATE player_data SET TOKENS = ? WHERE UUID = ?");
            ps.setInt(1, tokens);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentXp(UUID uuid, int currentXp) {
        try {
            PreparedStatement ps = con.getConnection()
                    .prepareStatement("UPDATE player_data SET CURRENTXP = ? WHERE UUID = ?");
            ps.setInt(1, currentXp);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setXpToNextLevel(UUID uuid, int xpToNextLevel) {
        try {
            PreparedStatement ps = con.getConnection()
                    .prepareStatement("UPDATE player_data SET XPTONEXTLEVEL = ? WHERE UUID = ?");
            ps.setInt(1, xpToNextLevel);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTokens(UUID uuid) throws SQLException {
        PreparedStatement ps = con.getConnection()
                .prepareStatement("SELECT * FROM player_data WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("TOKENS");
        } else {
            return -1;
        }
    }

    public int getAvailableTokens(UUID uuid) throws SQLException {
        PreparedStatement ps = con.getConnection()
                .prepareStatement("SELECT * FROM player_data WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("AVAILABLETOKENS");
        } else {
            return -1;
        }
    }

    public void setAvailableTokens(UUID uuid, int availableTokens) {
        try {
            PreparedStatement ps = con.getConnection()
                    .prepareStatement("UPDATE player_data SET AVAILABLETOKENS = ? WHERE UUID = ?");
            ps.setInt(1, availableTokens);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCurrentXp(UUID uuid) throws SQLException {
        PreparedStatement ps = con.getConnection()
                .prepareStatement("SELECT * FROM player_data WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("CURRENTXP");
        } else {
            return -1;
        }
    }

    public int getXpToNextLevel(UUID uuid) throws SQLException {
        PreparedStatement ps = con.getConnection()
                .prepareStatement("SELECT * FROM player_data WHERE UUID = ?");
        ps.setString(1, uuid.toString());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("XPTONEXTLEVEL");
        } else {
            return -1;
        }
    }

    public void giveTokenXp(Player p, int xpToAdd) { // level 4, 100 currentXp, 1900xpToNextLevel, i add 1900
        try {
            int currentXp = getCurrentXp(p.getUniqueId()); // xpToNextLevel = 100
            currentXp += xpToAdd; // 2000, xpToNextLevel = 1900
            setCurrentXp(p.getUniqueId(), currentXp);
            if (getCurrentXp(p.getUniqueId()) >= getXpToNextLevel(p.getUniqueId())) {
                int cnt = 0;
                while (currentXp - getXpToNextLevel(p.getUniqueId()) >= 0) {
                    currentXp -= getXpToNextLevel(p.getUniqueId());
                    setCurrentXp(p.getUniqueId(), currentXp); // 1500
                    tokenUp(p);
                    cnt++;
                }
            }
            String msg = ChatColorTranslator.color(PrisonCore.cfg.getString("messages.token_receive"));
            msg = msg.replace("%amount%", Integer.toString(xpToAdd));
            p.sendMessage(ChatColorTranslator.color(msg));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void tokenUp(Player player) {
        try {
            setTokens(player.getUniqueId(), getTokens(player.getUniqueId()) + 1);
            setAvailableTokens(player.getUniqueId(), getAvailableTokens(player.getUniqueId()) + 1);
            setXpToNextLevel(player.getUniqueId(), 2500 * getTokens(player.getUniqueId())); //1500
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            String msg = ChatColorTranslator.color(PrisonCore.cfg.getString("messages.token_level_up"));
            msg = msg.replace("%total%", Integer.toString(getTokens(player.getUniqueId())));
            msg = msg.replace("%amount%", Integer.toString(getAvailableTokens(player.getUniqueId())));
            player.sendMessage(ChatColorTranslator.color(msg));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
