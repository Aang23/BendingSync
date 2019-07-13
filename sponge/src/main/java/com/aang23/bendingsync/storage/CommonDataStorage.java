package com.aang23.bendingsync.storage;

import java.util.UUID;

import com.aang23.bendingsync.utils.InventorySerializer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import net.minecraft.entity.player.EntityPlayer;

public class CommonDataStorage {
    private final UUID uuid;
    private BendingDataStorage bendingStorage;
    private DSSDataStorage dssStorage;
    private String inventoryContent;

    public CommonDataStorage(Player player) {
        this.uuid = player.getUniqueId();
        bendingStorage = BendingDataStorage.getDataStorageFromBender((EntityPlayer) player);
        dssStorage = DSSDataStorage.getDataStorageFromPlayer((EntityPlayer) player);
        inventoryContent = InventorySerializer.serializeInventorytoJson(player);
    }

    public CommonDataStorage(UUID uuid, BendingDataStorage bendingData, DSSDataStorage dssData, String inventoryData) {
        this.uuid = uuid;
        bendingStorage = bendingData;
        dssStorage = dssData;
        inventoryContent = inventoryData;
    }

    public UUID getUuid() {
        return uuid;
    }

    public BendingDataStorage getBendingStorage() {
        return bendingStorage;
    }

    public void setBendingStorage(BendingDataStorage bendingStorage) {
        this.bendingStorage = bendingStorage;
    }

    public DSSDataStorage getDssStorage() {
        return dssStorage;
    }

    public void setDssStorage(DSSDataStorage dssStorage) {
        this.dssStorage = dssStorage;
    }

    public String getInventoryContent() {
        return inventoryContent;
    }

    public void setInventoryContent(String inventoryContent) {
        this.inventoryContent = inventoryContent;
    }
}