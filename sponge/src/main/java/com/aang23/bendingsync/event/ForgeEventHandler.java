package com.aang23.bendingsync.event;

import com.aang23.bendingsync.utils.BendingSyncUtils;
import com.crowsofwar.avatar.common.event.AbilityUseEvent;
import com.crowsofwar.avatar.common.event.AbilityLevelEvent;
import com.crowsofwar.avatar.common.event.AbilityUnlockEvent;
import com.crowsofwar.avatar.common.event.ElementRemoveEvent;
import com.crowsofwar.avatar.common.event.ElementUnlockEvent;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import io.github.nucleuspowered.nucleus.api.NucleusAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * Listener class for Forge's EventBus
 * 
 * @author Aang23
 */
public class ForgeEventHandler {
    @SubscribeEvent
    public static void onBending(AbilityUseEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            Player spongePlayer = (Player) event.getEntity();

            if (NucleusAPI.getFreezePlayerService().get().isFrozen(spongePlayer.getUniqueId())) {
                event.setCanceled(true);
                return;
            }

            if (spongePlayer.hasPermission("bendingsync.bending.disable"))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBendingLevelChange(AbilityLevelEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            Player player = (Player) event.getEntity();
            BendingSyncUtils.saveDataToDatabaseForPlayer(player);
        }
    }

    @SubscribeEvent
    public static void onBendingAbilityUnlock(AbilityUnlockEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            Player player = (Player) event.getEntity();
            BendingSyncUtils.saveDataToDatabaseForPlayer(player);
        }
    }

    @SubscribeEvent
    public static void onBendingElementRemove(ElementRemoveEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            Player player = (Player) event.getEntity();
            BendingSyncUtils.saveDataToDatabaseForPlayer(player);
        }
    }

    @SubscribeEvent
    public static void onBendingElementUnlock(ElementUnlockEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            Player player = (Player) event.getEntity();
            BendingSyncUtils.saveDataToDatabaseForPlayer(player);
        }
    }

    // TODO IMPROVE /!\ & Stop using this event!
    // Enable the override if this player got a permission for it.
    // TODO Implements perms such as bendingsync.override.waterbending to give a
    // specific bending (Waiting FavouriteDragon's methods)
    private static long ranTimes = 0;

    @SubscribeEvent
    public static void onTick(WorldTickEvent e) {
        ranTimes++;

        if (ranTimes % 20 == 0) {

            for (Player player : Sponge.getServer().getOnlinePlayers()) {
                boolean shouldOverride = player.hasPermission("bendingsync.override.enable");
                if (shouldOverride && !BendingSyncUtils.isDataOverriden(player))
                    BendingSyncUtils.setDataOverrideActive(player, true);
                else
                    BendingSyncUtils.setDataOverrideActive(player, false);

                BendingSyncUtils.sendNeatUpdatePacketFor(player);
            }
        }

        // Safety
        if (ranTimes == Long.MAX_VALUE)
            ranTimes = 0;
    }

    @SubscribeEvent
    public static void onItemDrop(ItemTossEvent event) {
        Player spongePlayer = (Player) event.getPlayer();
        if (NucleusAPI.getFreezePlayerService().get().isFrozen(spongePlayer.getUniqueId())) {
            event.getPlayer().inventory.addItemStackToInventory(event.getEntityItem().getItem());
            event.setCanceled(true);
        }
    }
}