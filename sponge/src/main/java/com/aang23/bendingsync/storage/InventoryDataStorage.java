package com.aang23.bendingsync.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.aang23.bendingsync.api.storage.IDataStorage;
import com.aang23.bendingsync.utils.ItemStackSerializer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InventoryDataStorage implements IDataStorage<InventoryDataStorage> {
    private JSONObject stacks = new JSONObject();

    public InventoryDataStorage fromJsonString(String in) {
        try {
            stacks = (JSONObject) new JSONParser().parse(in);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return this;
    }

    public InventoryDataStorage getFromPlayer(Player player) {
        EntityPlayer forgePlayer = (EntityPlayer) player;
        int inventory_size = forgePlayer.inventory.getSizeInventory();
        for (int i = 0; i < inventory_size; i++) {
            ItemStack currentStack = forgePlayer.inventory.getStackInSlot(i);
            if (!currentStack.isEmpty()) {
                String serialized = ItemStackSerializer.serializeToString(currentStack);
                //System.out.println(currentStack.toString() + " = " + serialized);
                stacks.put(i, serialized);
            }
        }
        return this;
    }

    public void restoreToPlayer(Player player) {
        EntityPlayer forgePlayer = (EntityPlayer) player;
        for (Entry<Integer, String> entry : ((Map<Integer, String>) stacks).entrySet()) {
            ItemStack currentStack = ItemStackSerializer.deserializeFromString(entry.getValue());
            forgePlayer.inventory.setInventorySlotContents(entry.getKey(), currentStack);
        }
    }

    public String toJsonString() {
        return stacks.toJSONString();
    }
}