package com.aang23.bendingsync.event;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.utils.BendingSyncUtils;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.world.SaveWorldEvent;

import me.ryanhamshire.griefprevention.api.event.BorderClaimEvent;

public class EventHandler {
    @Listener
    public void worldSaveEvent(SaveWorldEvent event) {
        // Save data more often
        for (Player player : event.getTargetWorld().getPlayers()) {
            BendingSyncUtils.saveDataToDatabaseForPlayer(player);
        }
    }

    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Join event) {
        // Apply data on login
        if (event.getSource() instanceof Player) {
            new Thread() {
                public void run() {
                    boolean wait = true;
                    while (wait) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                        if (!BendingSync.REDIS
                                .exists("sync_data_writting_" + event.getTargetEntity().getUniqueId().toString()))
                            wait = false;
                    }
                    BendingSyncUtils.applyDataFromDatabaseToPlayer((Player) event.getSource());
                }
            };
        }
    }

    @Listener
    public void onPlayerLogout(ClientConnectionEvent.Disconnect event) {
        // Save on logout
        if (event.getSource() instanceof Player) {
            BendingSync.REDIS.set("sync_data_writting_" + event.getTargetEntity().getUniqueId().toString(), "busy");
            BendingSyncUtils.saveDataToDatabaseForPlayer((Player) event.getSource());
            BendingSync.REDIS.del("sync_data_writting_" + event.getTargetEntity().getUniqueId().toString());
        }
    }

    @Listener
    public void onClaimEnter(BorderClaimEvent e, @First Player player) {

    }
}