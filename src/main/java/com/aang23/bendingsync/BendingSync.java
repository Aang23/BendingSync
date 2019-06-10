package com.aang23.bendingsync;

import com.aang23.bendingsync.mysql.MysqlUtils;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "bendingsync", name = "BendingSync", version = "1.0", description = "Syncs AV2 & DSS & ReSkillable")
public class BendingSync {

    @Inject
    public static Logger logger;

    @Listener
    public void onServerStart(GameAboutToStartServerEvent event) {
        ConfigManager.setupConfig();
        MysqlUtils.setup();
    }
}