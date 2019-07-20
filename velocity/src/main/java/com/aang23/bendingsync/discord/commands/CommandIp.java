package com.aang23.bendingsync.discord.commands;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.discord.DiscordCommand;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandIp implements DiscordCommand {

    @Override
    public String getCommandName() {
        return "ip";
    }

    @Override
    public String getCommandSyntax() {
        return "&ip";
    }

    @Override
    public String getCommandUsage() {
        return "Get the server's ip!";
    }

    @Override
    public int getCommandArgsCount() {
        return 1;
    }

    @Override
    public void executeCommand(String[] args, MessageChannel channel, User user) {
        channel.sendMessage("The ip (bound to change): www.altillimity.com").queue();
    }

}