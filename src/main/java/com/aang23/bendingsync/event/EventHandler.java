package com.aang23.bendingsync.event;

import com.aang23.bendingsync.utils.BendingSyncUtils;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.world.SaveWorldEvent;

import net.minecraft.entity.player.EntityPlayer;

public class EventHandler {
    @Listener
    public void worldSaveEvent(SaveWorldEvent event) {
        // Save data more often
        for (Player player : event.getTargetWorld().getPlayers()) {
            EntityPlayer mcPlayer = (EntityPlayer) player;
            BendingSyncUtils.saveDataToDatabaseForPlayer(mcPlayer);
        }
    }

    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Join event) {
        // Apply data on login
        if (event.getSource() instanceof Player) {
            EntityPlayer mcPlayer = (EntityPlayer) event.getSource();
            BendingSyncUtils.applyDataFromDatabaseToPlayer(mcPlayer);
        }
    }

    @Listener
    public void onPlayerLogout(ClientConnectionEvent.Disconnect event) {
        // Save on logout
        if (event.getSource() instanceof Player) {
            EntityPlayer mcPlayer = (EntityPlayer) event.getSource();
            BendingSyncUtils.saveDataToDatabaseForPlayer(mcPlayer);
        }
    }
}