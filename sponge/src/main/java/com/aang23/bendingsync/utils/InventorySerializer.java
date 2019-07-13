package com.aang23.bendingsync.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.spongepowered.api.entity.living.player.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventorySerializer {
    /**
     * Return a string which represents the content of this player's inventory
     * 
     * @param player
     * @return
     */
    public static String serializeInventorytoJson(Player player) {
        EntityPlayer forgePlayer = (EntityPlayer) player;
        JSONObject jsonResult = new JSONObject();
        int inventory_size = forgePlayer.inventory.getSizeInventory();
        for (int i = 0; i < inventory_size; i++) {
            ItemStack currentStack = forgePlayer.inventory.getStackInSlot(i);
            String serialized = ItemStackSerializer.serializeToString(currentStack);
            jsonResult.put(i, serialized);
        }
        return jsonResult.toJSONString();
    }

    /**
     * Restore the given player's inventory with the given data string
     * 
     * @param player
     * @param data
     */
    public static void deserializeInventoryToPlayer(Player player, String data) {
        EntityPlayer forgePlayer = (EntityPlayer) player;
        JSONObject jsonData = null;
        try {
            jsonData = (JSONObject) new JSONParser().parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int inventory_size = forgePlayer.inventory.getSizeInventory();
        for (int i = 0; i < inventory_size; i++) {
            String nbtData = (String) jsonData.get(i);
            ItemStack currentStack = ItemStackSerializer.deserializeFromString(nbtData);
            forgePlayer.inventory.setInventorySlotContents(i, currentStack);
        }
    }
}