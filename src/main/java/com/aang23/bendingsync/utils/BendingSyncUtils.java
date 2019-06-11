package com.aang23.bendingsync.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.mysql.MysqlUtils;
import com.aang23.bendingsync.storage.BendingDataStorage;
import com.aang23.bendingsync.storage.DSSDataStorage;
import com.aang23.bendingsync.storage.ReSkillableDataStorage;

import org.spongepowered.api.entity.living.player.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class BendingSyncUtils {
    private static List<String> bending_overrides = new ArrayList<String>();

    /**
     * Save everything that can be to the database for this player (Will do nothing
     * if the player has the override enabled)
     * 
     * @param player
     */
    public static void saveDataToDatabaseForPlayer(Player player) {
        if (isDataOverriden(player))
            return;

        EntityPlayer mcPlayer = (EntityPlayer) player;

        if (!MysqlUtils.doesBenderExists(mcPlayer.getCachedUniqueIdString()))
            MysqlUtils.addBender(mcPlayer.getCachedUniqueIdString(),
                    BendingDataStorage.getDataStorageFromBender(mcPlayer).toJsonString());
        else {
            MysqlUtils.delBender(mcPlayer.getCachedUniqueIdString());
            MysqlUtils.addBender(mcPlayer.getCachedUniqueIdString(),
                    BendingDataStorage.getDataStorageFromBender(mcPlayer).toJsonString());
        }

        if (!MysqlUtils.doesSwordsmanExists(mcPlayer.getCachedUniqueIdString()))
            MysqlUtils.addSwordsman(mcPlayer.getCachedUniqueIdString(),
                    DSSDataStorage.getDataStorageFromPlayer(mcPlayer).toJsonString());
        else {
            MysqlUtils.delSwordsman(mcPlayer.getCachedUniqueIdString());
            MysqlUtils.addSwordsman(mcPlayer.getCachedUniqueIdString(),
                    DSSDataStorage.getDataStorageFromPlayer(mcPlayer).toJsonString());
        }

        if (!MysqlUtils.doesReskillableUserExists(mcPlayer.getCachedUniqueIdString()))
            MysqlUtils.addReskillableUser(mcPlayer.getCachedUniqueIdString(),
                    ReSkillableDataStorage.getDataStorageFromPlayer(mcPlayer).toJsonString());
        else {
            MysqlUtils.delRekillableUser(mcPlayer.getCachedUniqueIdString());
            MysqlUtils.addReskillableUser(mcPlayer.getCachedUniqueIdString(),
                    ReSkillableDataStorage.getDataStorageFromPlayer(mcPlayer).toJsonString());
        }
    }

    /**
     * Apply datas from the database to the player (Will do nothing if the player
     * has the override enabled)
     * 
     * @param player
     */
    public static void applyDataFromDatabaseToPlayer(Player player) {
        if (isDataOverriden(player))
            return;

        EntityPlayer mcPlayer = (EntityPlayer) player;
        final ScheduledExecutorService exec1 = Executors.newScheduledThreadPool(1);

        exec1.schedule(new Runnable() {
            @Override
            public void run() {
                // Sync AV2
                if (MysqlUtils.doesBenderExists(mcPlayer.getCachedUniqueIdString())) {
                    BendingDataStorage storage = new BendingDataStorage();
                    storage.fromJsonString(MysqlUtils.getBendingData(mcPlayer.getCachedUniqueIdString()));
                    BendingDataStorage.setDataDromBendingStorage(mcPlayer, storage);
                } else
                    BendingSync.logger.info("No data for bender " + mcPlayer.getCachedUniqueIdString());
            }
        }, 4, TimeUnit.SECONDS);

        final ScheduledExecutorService exec2 = Executors.newScheduledThreadPool(1);

        exec2.schedule(new Runnable() {

            @Override
            public void run() {
                // Sync DSS
                if (MysqlUtils.doesSwordsmanExists(mcPlayer.getCachedUniqueIdString())) {
                    DSSDataStorage dssStorage = new DSSDataStorage();
                    dssStorage.fromJsonString(MysqlUtils.getDssData(mcPlayer.getCachedUniqueIdString()));
                    DSSDataStorage.setDataFromDSSStorage(mcPlayer, dssStorage);
                } else
                    BendingSync.logger.info("No data for swordsman " + mcPlayer.getCachedUniqueIdString());
            }
        }, 4, TimeUnit.SECONDS);

        final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);

        exec.schedule(new Runnable() {

            @Override
            public void run() {

                ((EntityPlayerMP) mcPlayer).getServerWorld().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        // Sync Reskillable
                        if (MysqlUtils.doesReskillableUserExists(mcPlayer.getCachedUniqueIdString())) {
                            ReSkillableDataStorage dssStorage = new ReSkillableDataStorage();
                            dssStorage.fromJsonString(MysqlUtils.getRekillableData(mcPlayer.getCachedUniqueIdString()));
                            ReSkillableDataStorage.setDataFromReSkillabletorage(mcPlayer, dssStorage);
                        } else
                            BendingSync.logger
                                    .info("No data for reskillable user " + mcPlayer.getCachedUniqueIdString());
                    }
                });
            }
        }, 4, TimeUnit.SECONDS);
    }

    /**
     * Enable / Disable the data override for a specific player This override allow
     * a plugin to temporarily modify a player's data (Bending / Abilities / DSS
     * Skills) with affecting his real datas.
     * 
     * Permissions are provided within this plugin to do so.
     * 
     * @param player
     * @param value
     */
    public static void setDataOverrideOn(Player player, boolean value) {
        String uuid = player.getUniqueId().toString();

        if (value) {
            if (!bending_overrides.contains(uuid))
                bending_overrides.add(uuid);
        } else {
            if (bending_overrides.contains(uuid))
                bending_overrides.remove(uuid);
        }
    }

    /**
     * Get if a player has the override enabled / disabled
     * 
     * @param player
     * @return
     */
    public static boolean isDataOverriden(Player player) {
        String uuid = player.getUniqueId().toString();
        return bending_overrides.contains(uuid);
    }
}