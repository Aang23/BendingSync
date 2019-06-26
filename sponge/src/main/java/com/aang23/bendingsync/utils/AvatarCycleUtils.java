package com.aang23.bendingsync.utils;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.mysql.MysqlUtils;
import com.crowsofwar.avatar.common.bending.BendingStyle;
import com.crowsofwar.avatar.common.bending.BendingStyles;
import com.crowsofwar.avatar.common.data.BendingData;

import org.spongepowered.api.entity.living.player.Player;

import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.Node;
import net.minecraft.entity.player.EntityPlayer;

public class AvatarCycleUtils {

    /**
     * Does not handle deleting the old avatar !
     * 
     * @param player
     */
    public static void setTheAvatar(Player player) {
        String uuid = player.getUniqueId().toString();

        MysqlUtils.setMetadataValue("avatar_uuid", uuid);

        BendingData data = BendingData.get((EntityPlayer) player);
        data.clearAbilityData();
        data.clearBending();

        Group avatarGroup = BendingSync.LUCKPERMS_API.getGroup("avatar");
        Node avatarGroupPerm = BendingSync.LUCKPERMS_API.getNodeFactory().makeGroupNode(avatarGroup).build();
        BendingSync.LUCKPERMS_API.getUser(player.getUniqueId()).setPermission(avatarGroupPerm);

        // Should give all bendings by default ? Should send explanations !
        for (BendingStyle style : BendingStyles.all())
            data.addBending(style);

        data.saveAll();

        BendingSyncUtils.saveDataToDatabaseForPlayer(player);

        BendingSyncUtils.broadcastOnWholeServer("&3" + player.getName() + " is the new Avatar !");
    }

    /**
     * Delete the current Avatar
     * 
     * @param player
     */
    public static void unsetTheAvatar(Player player) {
        MysqlUtils.setMetadataValue("avatar_uuid", "none");

        BendingData data = BendingData.get((EntityPlayer) player);
        data.clearAbilityData();
        data.clearBending();

        Group avatarGroup = BendingSync.LUCKPERMS_API.getGroup("avatar");
        Node avatarGroupPerm = BendingSync.LUCKPERMS_API.getNodeFactory().makeGroupNode(avatarGroup).build();
        BendingSync.LUCKPERMS_API.getUser(player.getUniqueId()).unsetPermission(avatarGroupPerm);

        data.saveAll();

        BendingSyncUtils.saveDataToDatabaseForPlayer(player);

        BendingSyncUtils.broadcastOnWholeServer("&3" + player.getName() + " is not the Avatar anymore...");
    }
}