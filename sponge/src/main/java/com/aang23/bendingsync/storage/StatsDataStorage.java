package com.aang23.bendingsync.storage;

import com.aang23.bendingsync.api.storage.IDataStorage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.spongepowered.api.entity.living.player.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.GameType;

/**
 * Storage class for a player's capabilities, gamemode, food stats and health
 * 
 * @author Aang23
 */
public class StatsDataStorage implements IDataStorage<StatsDataStorage> {
    private JSONObject stats = new JSONObject();

    public StatsDataStorage fromJsonString(String in) {
        try {
            stats = (JSONObject) new JSONParser().parse(in);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    public StatsDataStorage getFromPlayer(Player player) {
        EntityPlayer forgePlayer = (EntityPlayer) player;
        float health = forgePlayer.getHealth();
        int xp = forgePlayer.experienceTotal;
        int food = forgePlayer.getFoodStats().getFoodLevel();
        NBTTagCompound nbt = new NBTTagCompound();
        forgePlayer.capabilities.writeCapabilitiesToNBT(nbt);
        String capa = nbt.toString();
        int gm = ((EntityPlayerMP) forgePlayer).interactionManager.getGameType().getID();

        stats.put("Health", health);
        stats.put("Xp", xp);
        stats.put("Food", food);
        stats.put("Capabilities", capa);
        stats.put("Gamemode", gm);

        return this;
    }

    public void restoreToPlayer(Player player) {
        EntityPlayer forgePlayer = (EntityPlayer) player;

        float health = ((Double) stats.get("Health")).floatValue();
        int xp = ((Long) stats.get("Xp")).intValue();
        int food = ((Long) stats.get("Food")).intValue();
        String capa = (String) stats.get("Capabilities");
        int gm = ((Long) stats.get("Gamemode")).intValue();

        forgePlayer.setHealth(health);
        forgePlayer.experienceTotal = xp;
        forgePlayer.getFoodStats().setFoodLevel(food);

        NBTTagCompound nbt = null;
        try {
            nbt = (NBTTagCompound) JsonToNBT.getTagFromJson(capa);
        } catch (NBTException e) {
            e.printStackTrace();
        }

        forgePlayer.capabilities.readCapabilitiesFromNBT(nbt);
        forgePlayer.setGameType(GameType.getByID(gm));
    }

    public String toJsonString() {
        return stats.toJSONString();
    }
}