package com.aang23.bendingsync.api.storage;

import org.spongepowered.api.entity.living.player.Player;

public interface IDataStorage<T extends IDataStorage> {
    public String toJsonString();
    public T fromJsonString(String in);
    public T getFromPlayer(Player player);
    public void restoreToPlayer(Player player);
}