package com.aang23.bendingsync;

import org.spongepowered.api.plugin.Plugin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.aang23.bendingsync.mysql.MysqlUtils;
import com.crowsofwar.avatar.common.data.Bender;
import com.google.inject.Inject;
import org.slf4j.Logger;

@Plugin(id = "bendingsync", name = "BendingSync", version = "1.0", description = "Syncs AV2 & DSS & ReSkillable")
public class BendingSync {

    @Inject
    public static Logger logger;

    @Listener
    public void onServerStart(GameAboutToStartServerEvent event) {
        ConfigManager.setupConfig();
        MysqlUtils.setup();
    }

    // TODO : Interface for storages, get when to sync from Velocity (the proxy) instead of a delay, Merge utils & storage class ?
    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Join event) {
        if (event.getSource() instanceof Player) {
            EntityPlayer mcPlayer = (EntityPlayer) event.getSource();

            final ScheduledExecutorService exec1 = Executors.newScheduledThreadPool(1);

            exec1.schedule(new Runnable() {
                @Override
                public void run() {
                    // Sync AV2
                    if (MysqlUtils.doesBenderExists(mcPlayer.getCachedUniqueIdString())) {
                        BendingDataStorage storage = new BendingDataStorage();
                        storage.fromJsonString(MysqlUtils.getBendingData(mcPlayer.getCachedUniqueIdString()));
                        BendingDataUtils.setDataDromBendingStorage(Bender.get(mcPlayer), storage);
                    } else
                        logger.info("No data for bender " + mcPlayer.getCachedUniqueIdString());
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
                        DSSDataUtils.setDataFromDSSStorage(mcPlayer, dssStorage);
                    } else
                        logger.info("No data for swordsman " + mcPlayer.getCachedUniqueIdString());
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
                                dssStorage.fromJsonString(
                                        MysqlUtils.getRekillableData(mcPlayer.getCachedUniqueIdString()));
                                ReSkillableDataUtils.setDataFromReSkillabletorage(mcPlayer, dssStorage);
                            } else
                                logger.info("No data for reskillable user " + mcPlayer.getCachedUniqueIdString());
                        }
                    });
                }
            }, 4, TimeUnit.SECONDS);

        }
    }

    // TODO modify instead of deleting / adding.
    @Listener
    public void onPlayerLogout(ClientConnectionEvent.Disconnect event) {
        if (event.getSource() instanceof Player) {
            EntityPlayer mcPlayer = (EntityPlayer) event.getSource();

            if (!MysqlUtils.doesBenderExists(mcPlayer.getCachedUniqueIdString()))
                MysqlUtils.addBender(mcPlayer.getCachedUniqueIdString(),
                        BendingDataUtils.getDataStorageFromBender(Bender.get(mcPlayer)).toJsonString());
            else {
                MysqlUtils.delBender(mcPlayer.getCachedUniqueIdString());
                MysqlUtils.addBender(mcPlayer.getCachedUniqueIdString(),
                        BendingDataUtils.getDataStorageFromBender(Bender.get(mcPlayer)).toJsonString());
            }

            if (!MysqlUtils.doesSwordsmanExists(mcPlayer.getCachedUniqueIdString()))
                MysqlUtils.addSwordsman(mcPlayer.getCachedUniqueIdString(),
                        DSSDataUtils.getDataStorageFromPlayer(mcPlayer).toJsonString());
            else {
                MysqlUtils.delSwordsman(mcPlayer.getCachedUniqueIdString());
                MysqlUtils.addSwordsman(mcPlayer.getCachedUniqueIdString(),
                        DSSDataUtils.getDataStorageFromPlayer(mcPlayer).toJsonString());
            }

            if (!MysqlUtils.doesReskillableUserExists(mcPlayer.getCachedUniqueIdString()))
                MysqlUtils.addReskillableUser(mcPlayer.getCachedUniqueIdString(),
                        ReSkillableDataUtils.getDataStorageFromPlayer(mcPlayer).toJsonString());
            else {
                MysqlUtils.delRekillableUser(mcPlayer.getCachedUniqueIdString());
                MysqlUtils.addReskillableUser(mcPlayer.getCachedUniqueIdString(),
                        ReSkillableDataUtils.getDataStorageFromPlayer(mcPlayer).toJsonString());
            }

        }
    }
}