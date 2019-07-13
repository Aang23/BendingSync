package com.aang23.bendingsync.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackSerializer {
    /**
     * Returns a string which represents the given ItemStack
     * 
     * @param stack
     * @return
     */
    public static String serializeToString(ItemStack stack) {
        return stack.getTagCompound().toString();
    }

    /**
     * Return the ItemStack corresponding to the given data string
     * 
     * @param data
     * @return
     */
    public static ItemStack deserializeFromString(String data) {
        NBTTagCompound itemStackNbt = null;
        try {
            itemStackNbt = (NBTTagCompound) JsonToNBT.getTagFromJson(data);
        } catch (NBTException e) {
            e.printStackTrace();
        }

        if (itemStackNbt != null) {
            return new ItemStack(itemStackNbt);
        } else {
            return null;
        }
    }
}