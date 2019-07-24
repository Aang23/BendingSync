package com.aang23.bendingsync.event;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.utils.BendingSyncUtils;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.world.SaveWorldEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;

import io.github.nucleuspowered.nucleus.api.NucleusAPI;
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
            Player player = (Player) event.getSource();
            BendingSyncUtils.applyDataFromDatabaseToPlayer(player, 5);
            BendingSyncUtils.setToBeSynced(player);
            NucleusAPI.getFreezePlayerService().get().setFrozen(player, true);
            player.sendTitle(Title.of(Text.of("Please wait"),
                    Text.of("We are syncing your data! Your are frozen to prevent dupe bugs")));
        }
    }

    @Listener
    public void onPlayerLogout(ClientConnectionEvent.Disconnect event) {
        // Save on logout
        if (event.getSource() instanceof Player) {
            BendingSyncUtils.saveDataToDatabaseForPlayer((Player) event.getSource());
        }
    }

    @Listener
    public void onClaimEnter(BorderClaimEvent e, @First Player player) {

    }
}