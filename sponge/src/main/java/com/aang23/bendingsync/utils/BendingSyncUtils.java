package com.aang23.bendingsync.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.mysql.MysqlHandler;
import com.aang23.bendingsync.network.PlayerInfoPacket;
import com.aang23.bendingsync.storage.BendingDataStorage;
import com.aang23.bendingsync.storage.CommonDataStorage;
import com.aang23.bendingsync.storage.DSSDataStorage;
import com.aang23.bendingsync.storage.EffectsDataStorage;
import com.aang23.bendingsync.storage.InventoryDataStorage;
import com.crowsofwar.avatar.common.data.Bender;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;

import io.github.nucleuspowered.nucleus.api.NucleusAPI;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

public class BendingSyncUtils {
    private static List<String> bending_overrides = new ArrayList<String>();
    public static List<String> toBeSynced = new ArrayList<String>();

    /**
     * Save everything that can be to the database for this player (Will do nothing
     * if the player has the override enabled)
     * 
     * @param player
     */
    public static void saveDataToDatabaseForPlayer(Player player) {
        if (isDataOverriden(player) || isToBeSynced(player))
            return;

        BendingDataStorage bending = new BendingDataStorage().getFromPlayer(player);
        DSSDataStorage dss = new DSSDataStorage().getFromPlayer(player);
        InventoryDataStorage inventory = new InventoryDataStorage().getFromPlayer(player);
        EffectsDataStorage effects = new EffectsDataStorage().getFromPlayer(player);

        CommonDataStorage common = new CommonDataStorage(player.getUniqueId(), bending, dss, inventory, effects);

        MysqlHandler.saveStorage(common);
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
        String uuid = player.getUniqueId().toString();

        exec1.schedule(new Runnable() {
            @Override
            public void run() {
                // Run on the main server thread to prevent glitches
                ((EntityPlayerMP) mcPlayer).getServerWorld().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        if (MysqlHandler.doesPlayerExists(uuid)) {
                            System.out.println("Restoring!");
                            CommonDataStorage storage = MysqlHandler.getStorage(uuid);
                            storage.getBendingStorage().restoreToPlayer(player);
                            storage.getDssStorage().restoreToPlayer(player);
                            storage.getInventoryStorage().restoreToPlayer(player);
                            storage.getEffectsStorage().restoreToPlayer(player);
                            if (isToBeSynced(player)) {
                                toBeSynced.remove(uuid);
                                NucleusAPI.getFreezePlayerService().get().setFrozen(player, false);
                                player.sendTitle(Title.of(Text.of("Done!"), Text.of("You can now play")));
                            }
                        }
                    }
                });
            }
        }, delay, TimeUnit.SECONDS);
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
     * Get if a player wasn't synced yet
     * 
     * @param player
     * @return
     */
    public static boolean isToBeSynced(Player player) {
        String uuid = player.getUniqueId().toString();
        return toBeSynced.contains(uuid);
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

    /**
     * Sets a player as to be synced.
     * 
     * @param player
     */
    public static void setToBeSynced(Player player) {
        String uuid = player.getUniqueId().toString();
        toBeSynced.add(uuid);
    }
}