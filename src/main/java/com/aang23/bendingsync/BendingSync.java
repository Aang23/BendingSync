package com.aang23.bendingsync;

import com.aang23.bendingsync.commands.CommandRegistrar;
import com.aang23.bendingsync.event.EventHandler;
import com.aang23.bendingsync.event.ForgeEventHandler;
import com.aang23.bendingsync.mysql.MysqlUtils;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import io.github.nucleuspowered.nucleus.api.NucleusAPI;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.api.GriefPreventionApi;
import net.minecraftforge.common.MinecraftForge;

@Plugin(id = "bendingsync", name = "BendingSync", version = "1.0", description = "Syncs AV2 & DSS & ReSkillable", dependencies = {
        @Dependency(id = "griefprevention"), @Dependency(id = "luckperms") })
public class BendingSync {
    public static BendingSync INSTANCE;
    public static LuckPermsApi LUCKPERMS_API;
    public static GriefPreventionApi GRIEFPREVENTION_API;

    @Inject
    public static Logger logger;

    public BendingSync() {
        INSTANCE = this;
    }

    @Listener
    public void onForgePreInit(GameInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ForgeEventHandler.class);
        Sponge.getEventManager().registerListeners(this, new EventHandler());
        LUCKPERMS_API = LuckPerms.getApi();
        GRIEFPREVENTION_API = GriefPrevention.getApi();
    }

    @Listener
    public void onServerStart(GameAboutToStartServerEvent event) {
        CommandRegistrar.registerCommands();
        ConfigManager.setupConfig();
        MysqlUtils.setup();
    }
}