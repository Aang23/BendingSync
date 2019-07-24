package com.aang23.bendingsync.api.storage;

import org.spongepowered.api.entity.living.player.Player;

/**
 * Common interface for creating storage that are saved to the database
 * 
 * @author Aang23
 * @param <T>
 */
public interface IDataStorage<T extends IDataStorage> {
    /**
     * Serialize this object to a json string
     * 
     * @return
     */
    public String toJsonString();

    /**
     * Fill this object with the content of that json string
     * 
     * @param in
     * @return
     */
    public T fromJsonString(String in);

    /**
     * Fill that storage with this Player's data
     * 
     * @param player
     * @return
     */
    public T getFromPlayer(Player player);

    /**
     * Retore this storage's data to a player
     * 
     * @param player
     */
    public void restoreToPlayer(Player player);
}