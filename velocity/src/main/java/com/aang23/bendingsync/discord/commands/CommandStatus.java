package com.aang23.bendingsync.discord.commands;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.discord.DiscordCommand;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * @author Aang23
 */
public class CommandStatus implements DiscordCommand {

    @Override
    public String getCommandName() {
        return "status";
    }

    @Override
    public String getCommandSyntax() {
        return "&status";
    }

    @Override
    public String getCommandUsage() {
        return "Show the status of everything.";
    }

    @Override
    public int getCommandArgsCount() {
        return 1;
    }

    @Override
    public void executeCommand(String[] args, MessageChannel channel, User user) {
        String result = "Status:\nProxy - ONLINE";
        for(RegisteredServer server : BendingSync.server.getAllServers()){
            CompletableFuture<ServerPing> ping = server.ping();
            ping.join();
            result += server.getServerInfo().getName() + " - " + (ping.isCompletedExceptionally() ? "OFFLINE" : "ONLINE");
        }
        
        channel.sendMessage(result).queue();
    }

}