package com.aang23.bendingsync;

import java.util.Map.Entry;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.toast.ToastHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

public class ReSkillableDataUtils {
    public static ReSkillableDataStorage getDataStorageFromPlayer(EntityPlayer player) {
        ReSkillableDataStorage toreturn = new ReSkillableDataStorage();

        toreturn.userId = player.getCachedUniqueIdString();

        PlayerData resData = PlayerDataHandler.get(player);

        for (PlayerSkillInfo currentSkillInfo : resData.getAllSkillInfo()) {
            toreturn.levels.put(ReskillableRegistries.SKILLS.getKey(currentSkillInfo.skill).toString(),
                    String.valueOf(currentSkillInfo.getLevel()));
        }

        return toreturn;
    }

    public static void setDataFromReSkillabletorage(EntityPlayer player, ReSkillableDataStorage storage) {
        PlayerData resData = PlayerDataHandler.get(player);

        for (Entry<String, String> currentEntry : storage.levels.entrySet()) {
            Skill currentSkill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation(currentEntry.getKey()));
            PlayerSkillInfo currentSkillInfo = resData.getSkillInfo(currentSkill);
            int level = Integer.parseInt(currentEntry.getValue());
            int oldlevel = currentSkillInfo.getLevel();

            if (!MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Pre(player, currentSkill, level, oldlevel))) {
                currentSkillInfo.setLevel(level);
                resData.saveAndSync();
                MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Post(player, currentSkill, level, oldlevel));
                ToastHelper.sendSkillToast((EntityPlayerMP) player, currentSkill, level);
            }
        }
    }
}