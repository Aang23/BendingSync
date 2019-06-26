package com.aang23.bendingsync;

import com.aang23.bendingsync.commands.CommandRegistrar;
import com.aang23.bendingsync.event.EventHandler;
import com.aang23.bendingsync.event.ForgeEventHandler;
import com.aang23.bendingsync.mysql.MysqlUtils;
import com.aang23.bendingsync.network.PlayerInfoPacket;
import com.aang23.bendingsync.network.ServerSwitchPacket;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import me.ryanhamshire.griefprevention.GriefPrevention;
import me.ryanhamshire.griefprevention.api.GriefPreventionApi;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import redis.clients.jedis.Jedis;

@Plugin(id = "bendingsync", name = "BendingSync", version = "1.0", description = "Syncs AV2 & DSS & ReSkillable", dependencies = {
        @Dependency(id = "griefprevention"), @Dependency(id = "luckperms") })
public class BendingSync {
    public static BendingSync INSTANCE;
    public static LuckPermsApi LUCKPERMS_API;
    public static GriefPreventionApi GRIEFPREVENTION_API;
    public static SimpleNetworkWrapper NETWORK;
    public static Jedis REDIS;

    @Inject
    public static Logger logger;

    public BendingSync() {
        INSTANCE = this;
    }

    @Listener
    public void onForgePreInit(GameInitializationEvent event) {
        ConfigManager.setupConfig();
        MinecraftForge.EVENT_BUS.register(ForgeEventHandler.class);
        Sponge.getEventManager().registerListeners(this, new EventHandler());
        REDIS = new Jedis(ConfigManager.redis_address);
        LUCKPERMS_API = LuckPerms.getApi();
        GRIEFPREVENTION_API = GriefPrevention.getApi();
        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("bendingsync");
        NETWORK.registerMessage(PlayerInfoPacket.Handler.class, PlayerInfoPacket.class, 1, Side.CLIENT);
        NETWORK.registerMessage(ServerSwitchPacket.Handler.class, ServerSwitchPacket.class, 2, Side.SERVER);
    }

    @Listener
    public void onServerStart(GameAboutToStartServerEvent event) {
        CommandRegistrar.registerCommands();
        MysqlUtils.setup();
    }

    @Listener
    public void onServerStop(GameStoppingEvent event){
        REDIS.close();
    }
}