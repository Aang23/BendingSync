package com.aang23.bendingsync.utils;

import java.util.Optional;
import java.util.UUID;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.mysql.MysqlUtils;
import com.aang23.bendingsync.storage.BendingDataStorage;
import com.crowsofwar.avatar.common.bending.BendingStyle;
import com.crowsofwar.avatar.common.bending.BendingStyles;
import com.crowsofwar.avatar.common.data.BendingData;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.Node;
import net.minecraft.entity.player.EntityPlayer;

public class AvatarCycleUtils {

    private static void resetAvatarData(Player player) {
        BendingData data = BendingData.get((EntityPlayer) player);
        data.clearAbilityData();
        data.clearBending();
        for (BendingStyle style : BendingStyles.all())
            data.addBending(style);
        data.saveAll();
        saveAvatarData(player);
    }

    /**
     * Saves this player's data to the avatar datas
     * 
     * @param player
     */
    public static void saveAvatarData(Player player) {
        MysqlUtils.setMetadataValue("avatar_bending_data",
                BendingDataStorage.getDataStorageFromBender((EntityPlayer) player).toJsonString());
    }

    /**
     * Restores the Avatar's data to this player
     * 
     * @param player
     */
    public static void restoreAvatarData(Player player) {
        BendingDataStorage storage = new BendingDataStorage();
        storage.fromJsonString(MysqlUtils.getMedataValue("avatar_bending_data"));
        BendingDataStorage.setDataDromBendingStorage((EntityPlayer) player, storage);
    }

    /**
     * Check if this player is the avatar
     * 
     * @param player
     * @return
     */
    public static boolean isThisTheAvatar(Player player) {
        return MysqlUtils.getMedataValue("avatar_uuid").equals(player.getUniqueId().toString());
    }

    /**
     * Does not handle deleting the old avatar !
     * 
     * @param player
     */
    public static void setTheAvatar(Player player) {
        String uuid = player.getUniqueId().toString();

        MysqlUtils.setMetadataValue("avatar_uuid", uuid);

        // Safety
        resetAvatarData(player);
        saveAvatarData(player);
        restoreAvatarData(player);

        // For avatar-only things
        Group avatarGroup = BendingSync.LUCKPERMS_API.getGroup("avatar");
        Node avatarGroupPerm = BendingSync.LUCKPERMS_API.getNodeFactory().makeGroupNode(avatarGroup).build();
        BendingSync.LUCKPERMS_API.getUser(player.getUniqueId()).setPermission(avatarGroupPerm);

        // Should give all bendings by default ? Should send explanations !

        BendingSyncUtils.broadcastOnWholeServer("&3" + player.getName() + " is the new Avatar !");
    }

    /**
     * Delete the current Avatar
     * 
     * @param player
     */
    public static void unsetTheAvatar(Player player) {
        MysqlUtils.setMetadataValue("avatar_uuid", "none");

        Group avatarGroup = BendingSync.LUCKPERMS_API.getGroup("avatar");
        Node avatarGroupPerm = BendingSync.LUCKPERMS_API.getNodeFactory().makeGroupNode(avatarGroup).build();
        BendingSync.LUCKPERMS_API.getUser(player.getUniqueId()).unsetPermission(avatarGroupPerm);

        BendingSyncUtils.applyDataFromDatabaseToPlayer(player, 0);

        BendingSyncUtils.broadcastOnWholeServer("&3" + player.getName() + " is not the Avatar anymore...");
    }

    /**
     * Returns true if an Avatar exists
     * 
     * @return
     */
    public static boolean isThereAnAvatar() {
        return !MysqlUtils.getMedataValue("avatar_uuid").equals("none");
    }

    /**
     * Return the Avatar if online
     * 
     * @return
     */
    public static Optional<Player> getTheAvatar() {
        String uuid = MysqlUtils.getMedataValue("avatar_uuid");
        return Sponge.getServer().getPlayer(UUID.fromString(uuid));
    }
}