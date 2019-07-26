package com.aang23.bendingsync.event;

import java.util.List;
import java.util.function.Predicate;

import com.aang23.bendingsync.mysql.MysqlHandler;
import com.aang23.bendingsync.utils.BendingSyncUtils;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.event.world.SaveWorldEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;

import io.github.nucleuspowered.nucleus.api.NucleusAPI;
import me.ryanhamshire.griefprevention.api.event.BorderClaimEvent;
import net.minecraft.entity.item.EntityItem;

/**
 * Listener class for Sponge's EventBus
 * 
 * @author Aang23
 */
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
            if (MysqlHandler.doesPlayerExists(player.getUniqueId().toString())) {
                BendingSyncUtils.setToBeSynced(player);
                NucleusAPI.getFreezePlayerService().get().setFrozen(player, true);
                player.sendTitle(Title.of(Text.of("Please wait"),
                        Text.of("We are syncing your data! Your are frozen to prevent dupe bugs")));
            }
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

    @Listener
    public void onItemPickup(CollideEntityEvent event, @First Player player) {
        event.filterEntities(new Predicate<Entity>() {
            @Override
            public boolean test(Entity t) {
                if (t instanceof EntityItem) {
                    if (NucleusAPI.getFreezePlayerService().get().isFrozen(player.getUniqueId())) {
                        return false;
                    } else {
                        return true;
                    }
                } else
                    return true;
            }
        });
    }
}