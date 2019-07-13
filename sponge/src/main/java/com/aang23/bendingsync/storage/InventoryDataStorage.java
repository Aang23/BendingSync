package com.aang23.bendingsync.storage;

import java.util.HashMap;
import java.util.Map;

import com.aang23.bendingsync.api.storage.IDataStorage;
import com.aang23.bendingsync.utils.ItemStackSerializer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.spongepowered.api.entity.living.player.Player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventoryDataStorage implements IDataStorage<InventoryDataStorage> {
    private Map<Integer, ItemStack> stacks = new HashMap<Integer, ItemStack>();

    public InventoryDataStorage fromJsonString(String in) {
        JSONObject data = null;
        try {
            data = (JSONObject) new JSONParser().parse(in);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < data.size(); i++) {
            String nbt = (String) data.get(i);
            ItemStack currentStack = ItemStackSerializer.deserializeFromString(nbt);
            stacks.put(i, currentStack);
        }

        return this;
    }

    public InventoryDataStorage getFromPlayer(Player player) {
        EntityPlayer forgePlayer = (EntityPlayer) player;
        int inventory_size = forgePlayer.inventory.getSizeInventory();
        for (int i = 0; i < inventory_size; i++) {
            ItemStack currentStack = forgePlayer.inventory.getStackInSlot(i);
            stacks.put(i, currentStack);
        }
        return this;
    }

    public void restoreToPlayer(Player player) {
        EntityPlayer forgePlayer = (EntityPlayer) player;
        int inventory_size = forgePlayer.inventory.getSizeInventory();
        for (int i = 0; i < inventory_size; i++) {
            ItemStack currentStack = stacks.get(i);
            forgePlayer.inventory.setInventorySlotContents(i, currentStack);
        }
    }

    public String toJsonString() {
        JSONObject toReturn = new JSONObject();
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack currentStack = stacks.get(i);
            String data = ItemStackSerializer.serializeToString(currentStack);
            toReturn.put(i, data);
        }
        return toReturn.toJSONString();
    }
}