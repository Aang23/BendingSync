package com.aang23.bendingsync.storage;

import com.aang23.bendingsync.api.storage.IDataStorage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.spongepowered.api.entity.living.player.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Storage class for Potions effects
 * 
 * @author Aang23
 */
public class EffectsDataStorage implements IDataStorage<EffectsDataStorage> {
    private JSONObject effects = new JSONObject();

    public EffectsDataStorage fromJsonString(String in) {
        try {
            effects = (JSONObject) new JSONParser().parse(in);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    public EffectsDataStorage getFromPlayer(Player player) {
        EntityPlayer forgePlayer = (EntityPlayer) player;
        int i = 0;
        for (PotionEffect effect : forgePlayer.getActivePotionEffects()) {
            int amp = effect.getAmplifier();
            String name = effect.getPotion().getRegistryName().toString();
            int dur = effect.getDuration();
            JSONObject sub_effects = new JSONObject();
            sub_effects.put("Amp", amp);
            sub_effects.put("Dur", dur);
            sub_effects.put("Id", name);

            effects.put(i, sub_effects);
            i++;
        }
        return this;
    }

    public void restoreToPlayer(Player player) {
        EntityPlayer forgePlayer = (EntityPlayer) player;

        for (PotionEffect effect : forgePlayer.getActivePotionEffects())
            forgePlayer.removePotionEffect(effect.getPotion());

        for (int i = 0; true; i++) {
            JSONObject sub_effects = (JSONObject) effects.get(String.valueOf(i));
            if (sub_effects == null)
                break;
            int amp = ((Long) sub_effects.get("Amp")).intValue();
            String name = (String) sub_effects.get("Id");
            int dur = ((Long) sub_effects.get("Dur")).intValue();
            PotionEffect effect = new PotionEffect(ForgeRegistries.POTIONS.getValue(new ResourceLocation(name)), dur,
                    amp);
            forgePlayer.addPotionEffect(effect);
        }
    }

    public String toJsonString() {
        return effects.toJSONString();
    }
}