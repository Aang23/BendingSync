package com.aang23.bendingsync.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

public class ReSkillableDataStorage {
    public String userId;
    public Map<String, String> levels = new HashMap<String, String>();

    /**
     * Serialize this object to a JSON string
     * 
     * @return
     */
    public String toJsonString() {
        JSONObject data = new JSONObject();

        data.put("uuid", userId);
        data.put("levels", levels);

        return data.toJSONString();
    }

    /**
     * Restorage this object's content from a JSON string
     * 
     * @param in
     */
    public void fromJsonString(String in) {

        JSONObject data = null;
        try {
            data = (JSONObject) new JSONParser().parse(in);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        userId = (String) data.get("uuid");
        levels = (Map<String, String>) data.get("levels");
    }

    /**
     * Get a storage for this player
     * 
     * @param player
     * @return
     */
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

    /**
     * Apply a storage's datas to a player
     * 
     * @param player
     * @param storage
     */
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