package com.aang23.bendingsync.storage;

import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;

/**
 * Common class that store an instance of each storage type. Used to clean
 * things up
 * 
 * @author Aang23
 */
public class CommonDataStorage {
    private final UUID uuid;
    private BendingDataStorage bendingStorage;
    private DSSDataStorage dssStorage;
    private InventoryDataStorage inventoryStorage;
    private EffectsDataStorage effectsStorage;
    private StatsDataStorage statsStorage;
    private EnderChestDataStorage endStorage;

    public CommonDataStorage(Player player) {
        this.uuid = player.getUniqueId();
        bendingStorage = new BendingDataStorage().getFromPlayer(player);
        dssStorage = new DSSDataStorage().getFromPlayer(player);
        inventoryStorage = new InventoryDataStorage().getFromPlayer(player);
        effectsStorage = new EffectsDataStorage().getFromPlayer(player);
        statsStorage = new StatsDataStorage().getFromPlayer(player);
        endStorage = new EnderChestDataStorage().getFromPlayer(player);
    }

    public CommonDataStorage(UUID uuid, BendingDataStorage bendingData, DSSDataStorage dssData,
            InventoryDataStorage inventoryData, EffectsDataStorage effectsData, StatsDataStorage statsData,
            EnderChestDataStorage endData) {
        this.uuid = uuid;
        bendingStorage = bendingData;
        dssStorage = dssData;
        inventoryStorage = inventoryData;
        effectsStorage = effectsData;
        statsStorage = statsData;
        endStorage = endData;
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

    public StatsDataStorage getStatsStorage() {
        return statsStorage;
    }

    public void setStatsStorage(StatsDataStorage statsStorage) {
        this.statsStorage = statsStorage;
    }

    public EnderChestDataStorage getEndStorage() {
        return endStorage;
    }

    public void setEndStorage(EnderChestDataStorage endStorage) {
        this.endStorage = endStorage;
    }
}