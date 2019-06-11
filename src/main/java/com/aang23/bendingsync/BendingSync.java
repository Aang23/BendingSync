package com.aang23.bendingsync;

import com.aang23.bendingsync.commands.CommandRegistrar;
import com.aang23.bendingsync.mysql.MysqlUtils;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.PermissionDescription.Builder;

@Plugin(id = "bendingsync", name = "BendingSync", version = "1.0", description = "Syncs AV2 & DSS & ReSkillable", dependencies = {@Dependency(id = "griefprevention"),@Dependency(id = "luckperms")})
public class BendingSync {
    public static BendingSync INSTANCE;

    @Inject
    public static Logger logger;

    public BendingSync(){
        INSTANCE = this;
    }

    @Listener
    public void onServerStart(GameAboutToStartServerEvent event) {
        CommandRegistrar.registerCommands();
        ConfigManager.setupConfig();
        MysqlUtils.setup();
    }
}