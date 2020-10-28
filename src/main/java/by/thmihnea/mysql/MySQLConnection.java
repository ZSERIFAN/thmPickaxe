package by.thmihnea.mysql;

import by.thmihnea.PrisonCore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLConnection {
    public static Connection connection;
    public static String host, database, username, password;
    public static int port;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void createTable() {
        try {
            PreparedStatement ps = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS player_data (UUID varchar(256), TOKENS bigint(255), CURRENTXP bigint(255), XPTONEXTLEVEL bigint(255), AVAILABLETOKENS bigint(255))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sqlConnect() {
        host = PrisonCore.cfg.getString("mysql.host");
        port = PrisonCore.cfg.getInt("mysql.port");
        database = PrisonCore.cfg.getString("mysql.database");
        username = PrisonCore.cfg.getString("mysql.username");
        password = PrisonCore.cfg.getString("mysql.password");

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) return;
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
            }
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
