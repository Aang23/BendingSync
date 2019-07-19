package com.aang23.bendingsync;

import java.nio.file.Path;

import javax.security.auth.login.LoginException;

import com.google.common.io.ByteArrayDataInput;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import org.slf4j.Logger;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

@Plugin(id = "bendingsync", name = "BendingSync", version = "1.0", description = "A plugin", authors = { "Aang23" })
public class BendingSync {
    public static ProxyServer server;
    public static Logger logger;
    public static JDA jda;

    @Inject
    public BendingSync(ProxyServer lserver, CommandManager commandManager, EventManager eventManager, Logger llogger,
            @DataDirectory Path config_path) {

        ConfigManager.setupConfig(config_path);

        server = lserver;
        logger = llogger;

        try {
            jda = new JDABuilder(ConfigManager.discord_token).addEventListeners(new DiscordListener()).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

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
                } else if (args[0].equals("BroadCast")) {
                    server.broadcast(LegacyComponentSerializer.INSTANCE.deserialize(args[1], '&'));
                    server.getConsoleCommandSource()
                            .sendMessage(LegacyComponentSerializer.INSTANCE.deserialize(args[1], '&'));
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

    public class DiscordListener extends ListenerAdapter {
        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            if (event.getAuthor().isBot())
                return;
            // Handle commands & co here
        }
    }
}
