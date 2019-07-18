package com.aang23.bendingsync.mysql;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.storage.BendingDataStorage;
import com.aang23.bendingsync.storage.CommonDataStorage;
import com.aang23.bendingsync.storage.DSSDataStorage;
import com.aang23.bendingsync.storage.InventoryDataStorage;

import org.sql2o.ResultSetHandlerFactory;

public class MysqlHandler {
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
                .addParameter("uuid", uuid)
                .executeScalar(Boolean.class);
    }

    private static String getContentForUuidOf(String uuid, String column) {
        // @formatter:off
        String result = BendingSync.MYSQL.open().createQuery("SELECT * FROM players_data WHERE uuid=:uuid")
                                .addParameter("column", column)
                                .addParameter("uuid", uuid)
                                .executeAndFetch(String.class).toString();
        // @formatter:on
        System.out.println(result.toString());
        return result;
    }
}