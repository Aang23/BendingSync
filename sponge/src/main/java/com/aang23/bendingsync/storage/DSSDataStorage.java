package com.aang23.bendingsync.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.aang23.bendingsync.api.storage.IDataStorage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.spongepowered.api.entity.living.player.Player;

import dynamicswordskills.entity.DSSPlayerInfo;
import dynamicswordskills.skills.SkillBase;
import net.minecraft.entity.player.EntityPlayer;

public class DSSDataStorage implements IDataStorage<DSSDataStorage> {
    public Map<String, String> levels = new HashMap<String, String>();

    /**
     * Serialize this object to a JSON string
     * 
     * @return
     */
    public String toJsonString() {
        JSONObject data = new JSONObject();

        data.put("levels", levels);

        return data.toJSONString();
    }

    /**
     * Restorage this object's content from a JSON string
     * 
     * @param in
     */
    public DSSDataStorage fromJsonString(String in) {

        JSONObject data = null;
        try {
            data = (JSONObject) new JSONParser().parse(in);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        levels = (Map<String, String>) data.get("levels");

        return this;
    }

    /**
     * Returns a storage containing DSS data storage for this Player
     * 
     * @param player
     * @return
     */
    public DSSDataStorage getFromPlayer(Player spongePlayer) {
        EntityPlayer player = (EntityPlayer) spongePlayer;

        DSSDataStorage toreturn = new DSSDataStorage();

        DSSPlayerInfo dssInfos = DSSPlayerInfo.get(player);

        for (SkillBase currentSkill : SkillBase.getSkills()) {
            if (dssInfos.hasSkill(currentSkill)) {
                int level = dssInfos.getSkillLevel(currentSkill);
                toreturn.levels.put(currentSkill.getUnlocalizedName(), String.valueOf(level));
            }
        }

        return toreturn;
    }

    /**
     * Apply data from a storage to a player
     * 
     * @param player
     * @param storage
     */
    public void restoreToPlayer(Player spongePlayer) {
        EntityPlayer player = (EntityPlayer) spongePlayer;

        DSSPlayerInfo dssInfos = DSSPlayerInfo.get(player);
        dssInfos.resetSkills();

        for (Entry<String, String> currentSkillEntry : this.levels.entrySet()) {
            SkillBase currentSkill = SkillBase.getSkillByName(currentSkillEntry.getKey());
            int level = Integer.parseInt(currentSkillEntry.getValue());
            dssInfos.grantSkill(currentSkill);
            dssInfos.getPlayerSkill(currentSkill).grantSkill(player, level);
        }
    }
}