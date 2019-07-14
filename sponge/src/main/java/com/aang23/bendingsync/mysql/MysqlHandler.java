package com.aang23.bendingsync.mysql;

import java.util.List;
import java.util.UUID;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.storage.BendingDataStorage;
import com.aang23.bendingsync.storage.CommonDataStorage;
import com.aang23.bendingsync.storage.DSSDataStorage;
import com.aang23.bendingsync.storage.InventoryDataStorage;

public class MysqlHandler {
    public static void setupDatabase() {
        BendingSync.MYSQL.open().createQuery(
                "CREATE TABLE IF NOT EXISTS players_data (uuid VARCHAR(100), bending TEXT(10000), dss TEXT(10000), inventory TEXT(10000))")
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
                        + "ON DUPLICATE KEY UPDATE uuid=:uuid, bending=:bending, dss=:dss, inventory=:inventory")
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
        return BendingSync.MYSQL.createQuery("SELECT * FROM players_data WHERE uuid=':uuid'")
                .addParameter("uuid", uuid)
                .executeAndFetch(String.class).size() > 0;
    }

    private static String getContentForUuidOf(String uuid, String column) {
        // @formatter:off
        List<String> result = BendingSync.MYSQL.open().createQuery("SELECT :column FROM players_data WHERE bender_uuid=:uuid")
                                .addParameter("column", column)
                                .addParameter("uuid", uuid)
                                .executeAndFetch(String.class);
        // @formatter:on
        return result.size() > 0 ? result.get(0) : null;
    }
}