package com.aang23.bendingsync.event;

import com.aang23.bendingsync.BendingSync;
import com.crowsofwar.avatar.common.event.BendingEvent;

import org.spongepowered.api.entity.living.player.Player;

import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ForgeEventHandler {
    @SubscribeEvent
    public static void onBending(BendingEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            Player spongePlayer = (Player) event.getEntity();
            // TODO use luckperms !
            if (spongePlayer.hasPermission("bendingsync.bending.disable"))
                event.setCanceled(true);
        }
    }
}