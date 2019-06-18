package com.aang23.BendingSync;

import com.google.common.io.ByteArrayDataInput;
import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.api.proxy.server.ServerPing;

import org.slf4j.Logger;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;

@Plugin(id = "bendingsync", name = "BendingSync", version = "1.0", description = "A plugin", authors = { "Aang23" })
public class BendingSync {
    public static ProxyServer server;
    public static Logger logger;

    @Inject
    public BendingSync(ProxyServer lserver, CommandManager commandManager, EventManager eventManager, Logger llogger) {

        server = lserver;
        logger = llogger;
        logger.info("Loading BendingSync");

    }

    @Subscribe
    public void onInitialization(ProxyInitializeEvent event) {
        
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        /*if (!event.getIdentifier().equals(new LegacyChannelIdentifier("BendingSync"))) {
            return;
        }

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        if (!(event.getSource() instanceof ServerConnection)) {
            return;
        }

        ByteArrayDataInput in = event.dataAsDataStream();
        String subChannel = in.readUTF();

        if (subChannel.equals("Balance")) {
            String packet[] = in.readUTF().split(":");
            String username = packet[0];
            Double balance = Double.parseDouble(packet[1]);
            if (playerBalances.containsKey(username))
                playerBalances.replace(username, balance);
            else
                playerBalances.put(username, balance);
        }*/
    }
}
