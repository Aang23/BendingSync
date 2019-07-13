package com.aang23.bendingsync.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.aang23.bendingsync.BendingSync;
//import com.aang23.bendingsync.mysql.MysqlUtils;
import com.aang23.bendingsync.network.PlayerInfoPacket;
import com.aang23.bendingsync.storage.BendingDataStorage;
import com.aang23.bendingsync.storage.DSSDataStorage;
import com.crowsofwar.avatar.common.data.Bender;

import org.spongepowered.api.entity.living.player.Player;

import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;

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

        /*if (!AvatarCycleUtils.isThisTheAvatar(player)) {
            if (!MysqlUtils.doesBenderExists(mcPlayer.getCachedUniqueIdString()))
                MysqlUtils.addBender(mcPlayer.getCachedUniqueIdString(),
                        BendingDataStorage.getDataStorageFromBender(mcPlayer).toJsonString());
            else {
                MysqlUtils.delBender(mcPlayer.getCachedUniqueIdString());
                MysqlUtils.addBender(mcPlayer.getCachedUniqueIdString(),
                        BendingDataStorage.getDataStorageFromBender(mcPlayer).toJsonString());
            }
        } else
            AvatarCycleUtils.saveAvatarData(player);

        if (!MysqlUtils.doesSwordsmanExists(mcPlayer.getCachedUniqueIdString()))
            MysqlUtils.addSwordsman(mcPlayer.getCachedUniqueIdString(),
                    DSSDataStorage.getDataStorageFromPlayer(mcPlayer).toJsonString());
        else {
            MysqlUtils.delSwordsman(mcPlayer.getCachedUniqueIdString());
            MysqlUtils.addSwordsman(mcPlayer.getCachedUniqueIdString(),
                    DSSDataStorage.getDataStorageFromPlayer(mcPlayer).toJsonString());
        }*/
    }

    /**
     * Apply datas from the database to the player (Will do nothing if the player
     * has the override enabled)
     * 
     * @param player
     */
    public static void applyDataFromDatabaseToPlayer(Player player, int delay) {
        if (isDataOverriden(player))
            return;

        EntityPlayer mcPlayer = (EntityPlayer) player;
        final ScheduledExecutorService exec1 = Executors.newScheduledThreadPool(1);

        /*exec1.schedule(new Runnable() {
            @Override
            public void run() {
                // Sync AV2
                if (MysqlUtils.doesBenderExists(mcPlayer.getCachedUniqueIdString())) {
                    if (!AvatarCycleUtils.isThisTheAvatar(player)) {
                        BendingDataStorage storage = new BendingDataStorage();
                        storage.fromJsonString(MysqlUtils.getBendingData(mcPlayer.getCachedUniqueIdString()));
                        BendingDataStorage.setDataDromBendingStorage(mcPlayer, storage);
                    } else
                        AvatarCycleUtils.restoreAvatarData(player);
                } else
                    BendingSync.logger.info("No data for bender " + mcPlayer.getCachedUniqueIdString());
            }
        }, delay, TimeUnit.SECONDS);

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
        }, delay, TimeUnit.SECONDS);*/
    }

    /**
     * Enable / Disable the data override for a specific player This override allow
     * a plugin to temporarily modify a player's data (Bending / Abilities / DSS
     * Skills) without affecting his real datas.
     * 
     * Permissions are provided within this plugin to do so.
     * 
     * Disabling the override restores the Database's content
     * 
     * @param player
     * @param value
     */
    public static void setDataOverrideActive(Player player, boolean value) {
        String uuid = player.getUniqueId().toString();

        if (value) {
            if (!bending_overrides.contains(uuid)) {
                bending_overrides.add(uuid);
                Bender bender = Bender.get((EntityPlayer) player);
                bender.getData().clearAbilityData();
                bender.getData().clearBending();
            }
        } else {
            if (bending_overrides.contains(uuid)) {
                bending_overrides.remove(uuid);
                applyDataFromDatabaseToPlayer(player, 0);
            }
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

    /**
     * Send the Neat info packet to tracking entity for a specified player.
     * 
     * @param player
     */
    public static void sendNeatUpdatePacketFor(Player player) {
        EntityTracker et = ((WorldServer) player.getWorld()).getEntityTracker();
        String prefix = BendingSync.LUCKPERMS_API.getUser(player.getUniqueId()).getCachedData()
                .getMetaData(BendingSync.LUCKPERMS_API
                        .getContextForUser(BendingSync.LUCKPERMS_API.getUser(player.getUniqueId())).get())
                .getPrefix();
        if (prefix == null)
            prefix = "";
        et.sendToTracking((EntityPlayer) player,
                BendingSync.NETWORK.getPacketFrom(new PlayerInfoPacket(((EntityPlayer) player).getEntityId(), prefix)));
    }

    /**
     * Sends a message to the whole network
     * 
     * @param message
     */
    public static void broadcastOnWholeServer(String message) {
        BendingSync.REDIS.publish("bendingsync", "BroadCast:" + message);
    }
}