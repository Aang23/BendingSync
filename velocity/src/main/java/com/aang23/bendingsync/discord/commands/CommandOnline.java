package com.aang23.bendingsync.discord.commands;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.discord.DiscordCommand;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandOnline implements DiscordCommand {

    @Override
    public String getCommandName() {
        return "online";
    }

    @Override
    public String getCommandSyntax() {
        return "!online <player>";
    }

    @Override
    public String getCommandUsage() {
        return "Shows if a player is online";
    }

    @Override
    public int getCommandArgsCount() {
        return 2;
    }

    @Override
    public void executeCommand(String[] args, MessageChannel channel, User user) {
        boolean isOnline = BendingSync.server.getPlayer(args[1]).isPresent();
        channel.sendMessage("Player **" + args[1] + "** is " + (isOnline ? "online" : "offline") + ".")
                .queue();
    }

}