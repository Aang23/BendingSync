package com.aang23.bendingsync.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.Map.Entry;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.ConfigManager;
import com.aang23.bendingsync.storage.BendingDataStorage;
import com.aang23.bendingsync.storage.CommonDataStorage;
import com.aang23.bendingsync.storage.DSSDataStorage;
import com.aang23.bendingsync.storage.InventoryDataStorage;

import org.sql2o.ResultSetHandlerFactory;

public class MysqlHandler {
    private static Connection connection;
    private static Properties properties;

    public static void setupDatabase() {
        BendingSync.MYSQL.open().createQuery(
                "CREATE TABLE IF NOT EXISTS players_data (uuid VARCHAR(100), bending TEXT(10000), dss TEXT(10000), inventory TEXT(10000), PRIMARY KEY (`uuid`))")
                .executeUpdate();
    }

    public static void saveStorage(CommonDataStorage commonStorage) {
        String uuid = commonStorage.getUuid().toString();
        String bending = commonStorage.getBendingStorage().toJsonString();
        String dss = commonStorage.getDssStorage().toJsonString();
        String inv = commonStorage.getInventoryStorage().toJsonString();

        // @formatter:off
        BendingSync.MYSQL.open()
                .createQuery("INSERT INTO players_data (uuid, bending, dss, inventory) "
                        + "VALUES (:uuid, :bending, :dss, :inventory)  " 
                        + "ON DUPLICATE KEY UPDATE bending=:bending, dss=:dss, inventory=:inventory")
                .addParameter("uuid", uuid)
                .addParameter("bending", bending)
                .addParameter("dss", dss)
                .addParameter("inventory", inv)
                .executeUpdate();
        // @formatter:on
    }

    public static CommonDataStorage getStorage(String uuid) {
        BendingDataStorage bending = new BendingDataStorage();
        DSSDataStorage dss = new DSSDataStorage();
        InventoryDataStorage inventory = new InventoryDataStorage();

        bending.fromJsonString(getContentForUuidOf(uuid, "bending"));
        dss.fromJsonString(getContentForUuidOf(uuid, "dss"));
        inventory.fromJsonString(getContentForUuidOf(uuid, "inventory"));

        return new CommonDataStorage(UUID.fromString(uuid), bending, dss, inventory);
    }

    public static boolean doesPlayerExists(String uuid) {
        return BendingSync.MYSQL.open().createQuery("SELECT EXISTS(SELECT * FROM players_data WHERE uuid=:uuid)")
                .addParameter("uuid", uuid).executeScalar(Boolean.class);
    }

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
        System.out.println(result.toString());
        return result;
    }

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