package com.aang23.bendingsync.storage;

import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;

public class CommonDataStorage {
    private final UUID uuid;
    private BendingDataStorage bendingStorage;
    private DSSDataStorage dssStorage;
    private InventoryDataStorage inventoryStorage;
    private EffectsDataStorage effectsStorage;

    public CommonDataStorage(Player player) {
        this.uuid = player.getUniqueId();
        bendingStorage = new BendingDataStorage().getFromPlayer(player);
        dssStorage = new DSSDataStorage().getFromPlayer(player);
        inventoryStorage = new InventoryDataStorage().getFromPlayer(player);
        effectsStorage = new EffectsDataStorage().getFromPlayer(player);
    }

    public CommonDataStorage(UUID uuid, BendingDataStorage bendingData, DSSDataStorage dssData,
            InventoryDataStorage inventoryData, EffectsDataStorage effectsData) {
        this.uuid = uuid;
        bendingStorage = bendingData;
        dssStorage = dssData;
        inventoryStorage = inventoryData;
        effectsStorage = effectsData;
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

    public InventoryDataStorage getInventoryStorage() {
        return inventoryStorage;
    }

    public void setInventoryStorage(InventoryDataStorage inventoryStorage) {
        this.inventoryStorage = inventoryStorage;
    }

    public EffectsDataStorage getEffectsStorage() {
        return effectsStorage;
    }

    public void setEffectsStorage(EffectsDataStorage effectsStorage) {
        this.effectsStorage = effectsStorage;
    }
}