package com.aang23.bendingsync.discord.commands;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.discord.DiscordCommand;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandChat implements DiscordCommand {

    @Override
    public String getCommandName() {
        return "chat";
    }

    @Override
    public String getCommandSyntax() {
        return "&chat <server> <message>";
    }

    @Override
    public String getCommandUsage() {
        return "Send a chat message to this server";
    }

    @Override
    public int getCommandArgsCount() {
        return 3;
    }

    @Override
    public void executeCommand(String[] args, MessageChannel channel, User user) {
        String server_name = args[1];
        if (BendingSync.server.getServer(server_name).isPresent()) {
            String message = "";
            for (int i = 2; i < args.length; i++)
                message += args[i];
            BendingSync.REDIS.publish("bendingsync", "ChatOnServer:" + server_name + ":" + message);
        } else {
            channel.sendMessage("This server does not exist!").queue();
        }
    }

}