package com.aang23.bendingsync;

import java.util.Map.Entry;

import dynamicswordskills.entity.DSSPlayerInfo;
import dynamicswordskills.skills.SkillBase;
import net.minecraft.entity.player.EntityPlayer;

public class DSSDataUtils {
    public static DSSDataStorage getDataStorageFromPlayer(EntityPlayer player) {
        DSSDataStorage toreturn = new DSSDataStorage();

        toreturn.userId = player.getCachedUniqueIdString();

        DSSPlayerInfo dssInfos = DSSPlayerInfo.get(player);

        for (SkillBase currentSkill : SkillBase.getSkills()) {
            if (dssInfos.hasSkill(currentSkill)) {
                int level = dssInfos.getSkillLevel(currentSkill);
                toreturn.levels.put(currentSkill.getUnlocalizedName(), String.valueOf(level));
            }
        }

        return toreturn;
    }

    public static void setDataFromDSSStorage(EntityPlayer player, DSSDataStorage storage) {
        DSSPlayerInfo dssInfos = DSSPlayerInfo.get(player);
        dssInfos.resetSkills();

        for (Entry<String, String> currentSkillEntry : storage.levels.entrySet()) {
            SkillBase currentSkill = SkillBase.getSkillByName(currentSkillEntry.getKey());
            int level = Integer.parseInt(currentSkillEntry.getValue());
            dssInfos.grantSkill(currentSkill);
            dssInfos.getPlayerSkill(currentSkill).grantSkill(player, level);
        }
    }
}