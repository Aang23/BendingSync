package com.aang23.bendingsync.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.ConfigManager;
import com.aang23.bendingsync.storage.BendingDataStorage;
import com.aang23.bendingsync.storage.CommonDataStorage;
import com.aang23.bendingsync.storage.DSSDataStorage;
import com.aang23.bendingsync.storage.EffectsDataStorage;
import com.aang23.bendingsync.storage.EnderChestDataStorage;
import com.aang23.bendingsync.storage.InventoryDataStorage;
import com.aang23.bendingsync.storage.StatsDataStorage;

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
                "CREATE TABLE IF NOT EXISTS players_data (uuid VARCHAR(100), bending TEXT(10000), dss TEXT(10000), inventory TEXT(10000), effects TEXT(10000), stats TEXT(10000), end TEXT(10000), PRIMARY KEY (`uuid`))")
                .executeUpdate();
    }

    /**
     * Saves this CommonStorage to the DB
     * 
     * @param commonStorage
     */
    public static void saveStorage(CommonDataStorage commonStorage) {
        String uuid = commonStorage.getUuid().toString();
        String bending = commonStorage.getBendingStorage().toJsonString();
        String dss = commonStorage.getDssStorage().toJsonString();
        String inv = commonStorage.getInventoryStorage().toJsonString();
        String eff = commonStorage.getEffectsStorage().toJsonString();
        String sta = commonStorage.getStatsStorage().toJsonString();
        String end = commonStorage.getEndStorage().toJsonString();

        // @formatter:off
        BendingSync.MYSQL.open()
                .createQuery("INSERT INTO players_data (uuid, bending, dss, inventory, effects, stats, end) "
                        + "VALUES (:uuid, :bending, :dss, :inventory, :effects, :stats)  " 
                        + "ON DUPLICATE KEY UPDATE bending=:bending, dss=:dss, inventory=:inventory, effects=:effects, stats=:stats, end=:end")
                .addParameter("uuid", uuid)
                .addParameter("bending", bending)
                .addParameter("dss", dss)
                .addParameter("inventory", inv)
                .addParameter("effects", eff)
                .addParameter("stats", sta)
                .addParameter("end", end)
                .executeUpdate();
        // @formatter:on
    }

    /**
     * Returns the CommonStorage for a given UUID. Loaded from the DB
     * 
     * @param uuid
     * @return
     */
    public static CommonDataStorage getStorage(String uuid) {
        BendingDataStorage bending = new BendingDataStorage();
        DSSDataStorage dss = new DSSDataStorage();
        InventoryDataStorage inventory = new InventoryDataStorage();
        EffectsDataStorage effects = new EffectsDataStorage();
        StatsDataStorage stats = new StatsDataStorage();
        EnderChestDataStorage end = new EnderChestDataStorage();

        bending.fromJsonString(getContentForUuidOf(uuid, "bending"));
        dss.fromJsonString(getContentForUuidOf(uuid, "dss"));
        inventory.fromJsonString(getContentForUuidOf(uuid, "inventory"));
        effects.fromJsonString(getContentForUuidOf(uuid, "effects"));
        stats.fromJsonString(getContentForUuidOf(uuid, "stats"));
        end.fromJsonString(getContentForUuidOf(uuid, "end"));

        return new CommonDataStorage(UUID.fromString(uuid), bending, dss, inventory, effects, stats, end);
    }

    /**
     * Check if this player exists in the DB
     * 
     * @param uuid
     * @return
     */
    public static boolean doesPlayerExists(String uuid) {
        return BendingSync.MYSQL.open().createQuery("SELECT EXISTS(SELECT * FROM players_data WHERE uuid=:uuid)")
                .addParameter("uuid", uuid).executeScalar(Boolean.class);
    }

    /**
     * get a specified column for that player
     * 
     * @param uuid
     * @param column
     * @return
     */
    private static String getContentForUuidOf(String uuid, String column) {
        String result = null;
        String sql = "SELECT " + column + " FROM players_data WHERE uuid='" + uuid + "';";
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