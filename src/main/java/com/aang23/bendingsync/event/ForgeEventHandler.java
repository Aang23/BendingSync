package com.aang23.bendingsync.event;

import com.aang23.bendingsync.BendingSync;
import com.crowsofwar.avatar.common.event.BendingEvent;

import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import io.github.nucleuspowered.nucleus.api.NucleusAPI;

public class ForgeEventHandler {
    private static Node preventBendingNode = BendingSync.LUCKPERMS_API.getNodeFactory()
            .newBuilder("bendingsync.bending.disable").build();

    @SubscribeEvent
    public static void onBending(BendingEvent event) {
        // Using LuckPerms' API... I hope contexts gets updated faster here
        User permUser = BendingSync.LUCKPERMS_API.getUser(event.getEntity().getUniqueID());
        if (permUser.hasPermission(preventBendingNode).asBoolean())
            event.setCanceled(true);

    }
}