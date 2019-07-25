package com.aang23.bendingsync.storage;

import com.aang23.bendingsync.api.storage.IDataStorage;
import com.aang23.bendingsync.utils.ItemStackSerializer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.spongepowered.api.entity.living.player.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Storage class for the Ender Chest
 * 
 * @author Aang23
 */
public class EnderChestDataStorage implements IDataStorage<EnderChestDataStorage> {
    private JSONObject stacks = new JSONObject();

    public EnderChestDataStorage fromJsonString(String in) {
        try {
            stacks = (JSONObject) new JSONParser().parse(in);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    public EnderChestDataStorage getFromPlayer(Player player) {
        EntityPlayer forgePlayer = (EntityPlayer) player;
        int inventory_size = forgePlayer.getInventoryEnderChest().getSizeInventory();
        for (int i = 0; i < inventory_size; i++) {
            ItemStack currentStack = forgePlayer.getInventoryEnderChest().getStackInSlot(i);
            if (!currentStack.isEmpty()) {
                String serialized = ItemStackSerializer.serializeToString(currentStack);
                // System.out.println(currentStack.toString() + " = " + serialized);
                stacks.put(i, serialized);
            }
        }
        return this;
    }

    public void restoreToPlayer(Player player) {
        EntityPlayer forgePlayer = (EntityPlayer) player;
        int inventory_size = forgePlayer.getInventoryEnderChest().getSizeInventory();
        for (int i = 0; i < inventory_size; i++) {
            String item = (String) stacks.get(String.valueOf(i));
            if (item == null) {
                forgePlayer.getInventoryEnderChest().setInventorySlotContents(i, ItemStack.EMPTY);
            } else {
                ItemStack currentStack = ItemStackSerializer.deserializeFromString(item);
                forgePlayer.getInventoryEnderChest().setInventorySlotContents(i, currentStack);
            }
        }
    }

    public String toJsonString() {
        return stacks.toJSONString();
    }
}