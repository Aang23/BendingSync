package com.aang23.bendingsync.discord.commands;

import java.util.Optional;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.discord.DiscordCommand;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandWhereIs implements DiscordCommand {

    @Override
    public String getCommandName() {
        return "whereis";
    }

    @Override
    public String getCommandSyntax() {
        return "&whereis <player>";
    }

    @Override
    public String getCommandUsage() {
        return "Shows which server a player is on";
    }

    @Override
    public int getCommandArgsCount() {
        return 2;
    }

    @Override
    public void executeCommand(String[] args, MessageChannel channel, User user) {
        boolean isOnline = BendingSync.server.getPlayer(args[1]).isPresent();
        if (isOnline) {
            Optional<ServerConnection> server = BendingSync.server.getPlayer(args[1]).get().getCurrentServer();
            channel.sendMessage("Player **" + args[1] + "** is on the server **"
                    + (server.isPresent() ? server.get().getServerInfo().getName() : "null") + "**.").queue();
        } else {
            channel.sendMessage("This player isn't online.").queue();
        }
    }

}