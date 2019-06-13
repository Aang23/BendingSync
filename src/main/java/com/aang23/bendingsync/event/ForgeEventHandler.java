package com.aang23.bendingsync.event;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.network.NeatInfoPacket;
import com.aang23.bendingsync.utils.BendingSyncUtils;
import com.crowsofwar.avatar.common.event.BendingEvent;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class ForgeEventHandler {

    @SubscribeEvent
    public static void onBending(BendingEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            Player spongePlayer = (Player) event.getEntity(); // TODO use luckperms ! if
            if (spongePlayer.hasPermission("bendingsync.bending.disable"))
                event.setCanceled(true);
        }
    }

    // TODO IMPROVE /!\ & Stop using this event!
    // Enable the override if this player got a permission for it.
    // TODO Implements perms such as bendingsync.override.waterbending to give a
    // specific bending (Waiting FavouriteDragon's methods)
    // Also reset datas when activating the override
    @SubscribeEvent
    public static void onTick(WorldTickEvent e) {
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            boolean shouldOverride = player.hasPermission("bendingsync.override.enable");
            if (shouldOverride && !BendingSyncUtils.isDataOverriden(player))
                BendingSyncUtils.setDataOverrideActive(player, true);
            else
                BendingSyncUtils.setDataOverrideActive(player, false);

            EntityTracker et = ((WorldServer) player.getWorld()).getEntityTracker();
            String prefix = BendingSync.LUCKPERMS_API.getUser(player.getUniqueId()).getCachedData()
                    .getMetaData(BendingSync.LUCKPERMS_API
                            .getContextForUser(BendingSync.LUCKPERMS_API.getUser(player.getUniqueId())).get())
                    .getPrefix();

            if (prefix == null)
                prefix = "";
            et.sendToTracking((EntityPlayer) player, BendingSync.NETWORK
                    .getPacketFrom(new NeatInfoPacket(((EntityPlayer) player).getEntityId(), prefix)));
        }
    }
}