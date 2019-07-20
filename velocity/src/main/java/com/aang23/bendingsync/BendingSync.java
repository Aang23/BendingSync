package com.aang23.bendingsync;

import java.nio.file.Path;

import javax.security.auth.login.LoginException;

import com.aang23.bendingsync.discord.DiscordCommands;
import com.aang23.bendingsync.discord.commands.CommandChat;
import com.aang23.bendingsync.discord.commands.CommandIp;
import com.aang23.bendingsync.discord.commands.CommandOnline;
import com.aang23.bendingsync.discord.commands.CommandPlayerCount;
import com.aang23.bendingsync.discord.commands.CommandPlayerList;
import com.aang23.bendingsync.discord.commands.CommandWhereIs;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import org.slf4j.Logger;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

@Plugin(id = "bendingsync", name = "BendingSync", version = "1.0", description = "A plugin", authors = { "Aang23" })
public class BendingSync {
    public static ProxyServer server;
    public static Logger logger;
    public static JDA jda;
    public static Jedis JEDIS;

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

        DiscordCommands.registerCommand(new CommandOnline());
        DiscordCommands.registerCommand(new CommandPlayerList());
        DiscordCommands.registerCommand(new CommandIp());
        DiscordCommands.registerCommand(new CommandPlayerCount());
        DiscordCommands.registerCommand(new CommandWhereIs());
        DiscordCommands.registerCommand(new CommandChat());
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
                    TextComponent text = LegacyComponentSerializer.INSTANCE.deserialize(args[1], '&');
                    server.broadcast(text);
                    server.getConsoleCommandSource().sendMessage(text);
                    jda.getTextChannelById(ConfigManager.channelid).sendMessage(text.toString());
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Jedis jedis = new Jedis("192.168.1.16");
                    JEDIS = jedis;
                    jedis.subscribe(subscriber, "bendingsync");
                    jedis.quit();
                    logger.error("Redis disconnected !");
                    jedis.close();
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
            if (event.getChannelType() == ChannelType.TEXT
                    && event.getChannel().getId().equals(ConfigManager.channelid)) {
                DiscordCommands.runCommand(event);
            }
        }
    }
}
