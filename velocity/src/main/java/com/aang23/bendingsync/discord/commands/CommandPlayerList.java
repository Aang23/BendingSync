package com.aang23.bendingsync.discord.commands;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.discord.DiscordCommand;
import com.velocitypowered.api.proxy.Player;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandPlayerList implements DiscordCommand {

    @Override
    public String getCommandName() {
        return "playerlist";
    }

    @Override
    public String getCommandSyntax() {
        return "&playerlist";
    }

    @Override
    public String getCommandUsage() {
        return "List online players";
    }

    @Override
    public int getCommandArgsCount() {
        return 1;
    }

    @Override
    public void executeCommand(String[] args, MessageChannel channel, User user) {
        if (BendingSync.server.getAllPlayers().size() > 0) {
            String list = "Online players :\n";
            for (Player player : BendingSync.server.getAllPlayers())
                list += player.getUsername() + "\n";
            channel.sendMessage(list).queue();
        } else {
            channel.sendMessage("No one is online at the moment.").queue();
        }
    }

}