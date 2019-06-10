package com.aang23.bendingsync.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.mysql.MysqlUtils;
import com.aang23.bendingsync.storage.BendingDataStorage;
import com.aang23.bendingsync.storage.DSSDataStorage;
import com.aang23.bendingsync.storage.ReSkillableDataStorage;
import com.crowsofwar.avatar.common.data.Bender;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class BendingSyncUtils {
    /**
     * Save everything that can be to the database for this player
     * 
     * @param mcPlayer
     */
    public static void saveDataToDatabaseForPlayer(EntityPlayer mcPlayer) {
        if (!MysqlUtils.doesBenderExists(mcPlayer.getCachedUniqueIdString()))
            MysqlUtils.addBender(mcPlayer.getCachedUniqueIdString(),
                    BendingDataStorage.getDataStorageFromBender(Bender.get(mcPlayer)).toJsonString());
        else {
            MysqlUtils.delBender(mcPlayer.getCachedUniqueIdString());
            MysqlUtils.addBender(mcPlayer.getCachedUniqueIdString(),
                    BendingDataStorage.getDataStorageFromBender(Bender.get(mcPlayer)).toJsonString());
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
     * Apply datas from the database to the player
     * 
     * @param mcPlayer
     */
    public static void applyDataFromDatabaseToPlayer(EntityPlayer mcPlayer) {
        final ScheduledExecutorService exec1 = Executors.newScheduledThreadPool(1);

        exec1.schedule(new Runnable() {
            @Override
            public void run() {
                // Sync AV2
                if (MysqlUtils.doesBenderExists(mcPlayer.getCachedUniqueIdString())) {
                    BendingDataStorage storage = new BendingDataStorage();
                    storage.fromJsonString(MysqlUtils.getBendingData(mcPlayer.getCachedUniqueIdString()));
                    BendingDataStorage.setDataDromBendingStorage(Bender.get(mcPlayer), storage);
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
}