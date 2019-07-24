package com.aang23.bendingsync.discord.commands;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.discord.DiscordCommand;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * @author Aang23
 */
public class CommandServerList implements DiscordCommand {

    @Override
    public String getCommandName() {
        return "serverlist";
    }

    @Override
    public String getCommandSyntax() {
        return "&serverlist";
    }

    @Override
    public String getCommandUsage() {
        return "List all servers";
    }

    @Override
    public int getCommandArgsCount() {
        return 1;
    }

    @Override
    public void executeCommand(String[] args, MessageChannel channel, User user) {
        String list = "All servers :\n";
        for (RegisteredServer server : BendingSync.server.getAllServers())
            list += server.getServerInfo().getName() + "\n";
        channel.sendMessage(list).queue();
    }

}