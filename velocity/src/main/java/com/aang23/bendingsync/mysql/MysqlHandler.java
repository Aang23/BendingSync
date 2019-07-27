package com.aang23.bendingsync.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.ConfigManager;

/**
 * MySQL helper class. Contains wrapper methods & more
 * 
 * @author Aang23
 */
public class MysqlHandler {
    private static Connection connection;
    private static Properties properties;

    /**
     * Create default tables
     */
    public static void setupDatabase() {
        BendingSync.MYSQL.open().createQuery(
                "CREATE TABLE IF NOT EXISTS banned_players (uuid VARCHAR(100), name VARCHAR(100), reason TEXT(10000), PRIMARY KEY (`uuid`))")
                .executeUpdate();
    }

    public static void setUserban(String uuid, String name, String reason) {
        // @formatter:off
        BendingSync.MYSQL.open()
                .createQuery("INSERT INTO banned_players (uuid, name, reason) "
                        + "VALUES (:uuid, :name, :reason) " 
                        + "ON DUPLICATE KEY UPDATE uuid=:uuid name=:name reason=:reason")
                .addParameter("uuid", uuid)
                .addParameter("reason", reason)
                .addParameter("name", name)
                .executeUpdate();
        // @formatter:on
    }

    public static void setUserunbanUuid(String uuid) {
        // @formatter:off
        BendingSync.MYSQL.open()
                .createQuery("DELETE FROM banned_players WHERE uuid=:uuid ")
                .addParameter("uuid", uuid)
                .executeUpdate();
        // @formatter:on
    }

    public static void setUserunbanUsername(String username) {
        // @formatter:off
        BendingSync.MYSQL.open()
                .createQuery("DELETE FROM banned_players WHERE name=:name ")
                .addParameter("name", username)
                .executeUpdate();
        // @formatter:on
    }

    /**
     * Check if this player is banned
     * 
     * @param uuid
     * @return
     */
    public static boolean isPlayerBannedUuid(String uuid) {
        return BendingSync.MYSQL.open().createQuery("SELECT EXISTS(SELECT * FROM banned_players WHERE uuid=:uuid)")
                .addParameter("uuid", uuid).executeScalar(Boolean.class);
    }

    /**
     * Check if this player is banned
     * 
     * @param uuid
     * @return
     */
    public static boolean isPlayerBannedName(String name) {
        return BendingSync.MYSQL.open().createQuery("SELECT EXISTS(SELECT * FROM banned_players WHERE name=:name)")
                .addParameter("name", name).executeScalar(Boolean.class);
    }

    public static String getBanReason(String uuid) {
        return getContentForUuidOf(uuid, "reason", "banned_players");
    }

    /**
     * Get the content of a colum in a table
     * 
     * @param uuid
     * @param column
     * @param database
     * @return
     */
    private static String getContentForUuidOf(String uuid, String column, String database) {
        String result = null;
        String sql = "SELECT " + column + " FROM " + database + " WHERE uuid='" + uuid + "';";
        try {
            PreparedStatement statement = connectStandard().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            rs.next();
            result = rs.getString(column);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnectStandard();
        }
        return result;
    }

    /**
     * Get a standard Java connection to the DB
     * 
     * @return
     */
    public static Connection connectStandard() {
        String DATABASE_URL = "jdbc:mysql://" + ConfigManager.address + ":" + ConfigManager.port + "/"
                + ConfigManager.database
                + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Disconnects the standard connection
     */
    public static void disconnectStandard() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", ConfigManager.username);
            properties.setProperty("password", ConfigManager.password);
            properties.setProperty("MaxPooledStatements", "250");
        }
        return properties;
    }
}