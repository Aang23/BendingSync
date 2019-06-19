package com.aang23.bendingsync;

import com.google.common.io.ByteArrayDataInput;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import org.slf4j.Logger;

@Plugin(id = "bendingsync", name = "BendingSync", version = "1.0", description = "A plugin", authors = { "Aang23" })
public class BendingSync {
    public static ProxyServer server;
    public static Logger logger;

    @Inject
    public BendingSync(ProxyServer lserver, CommandManager commandManager, EventManager eventManager, Logger llogger) {

        server = lserver;
        logger = llogger;
        logger.info("Loading BendingSync");

        setupRedisSubscriber();
    }

    @Subscribe
    public void onInitialization(ProxyInitializeEvent event) {

    }

    private void setupRedisSubscriber() {
        final JedisPubSub subscriber = new JedisPubSub() {
            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
            }

            @Override
            public void onPUnsubscribe(String pattern, int subscribedChannels) {
            }

            @Override
            public void onPSubscribe(String pattern, int subscribedChannels) {
            }

            @Override
            public void onPMessage(String pattern, String channel, String message) {
            }

            @Override
            public void onMessage(String channel, String message) {
                if (!channel.equals("bendingsync"))
                    return;
                String args[] = message.split(":");
                if (args[0].equals("SendToServer")) {
                    if (server.getPlayer(args[1]).isPresent() && server.getServer(args[2]).isPresent()) {
                        Player player = server.getPlayer(args[1]).get();
                        RegisteredServer info = server.getServer(args[2]).get();
                        player.createConnectionRequest(info).fireAndForget();
                    }
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Jedis jedis = new Jedis("192.168.1.16");
                    jedis.subscribe(subscriber, "bendingsync");
                    jedis.quit();
                    logger.error("Redis disconnected !");
                } catch (Exception e) {
                }
            }
        }).start();
    }
}
