package com.aang23.bendingsync.discord.commands;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.discord.DiscordCommand;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * @author Aang23
 */
public class CommandPlayerCount implements DiscordCommand {

    @Override
    public String getCommandName() {
        return "playercount";
    }

    @Override
    public String getCommandSyntax() {
        return "&playercount [server]";
    }

    @Override
    public String getCommandUsage() {
        return "Shows how many players are connected to the whole network or a specific server";
    }

    @Override
    public int getCommandArgsCount() {
        return 1;
    }

    @Override
    public void executeCommand(String[] args, MessageChannel channel, User user) {
        if (args.length == 1) {
            channel.sendMessage("Currently, " + BendingSync.server.getPlayerCount() + " out of "
                    + BendingSync.server.getConfiguration().getShowMaxPlayers() + " are online.").queue();
        } else {
            if (BendingSync.server.getServer(args[1]).isPresent()) {
                RegisteredServer server = BendingSync.server.getServer(args[1]).get();
                channel.sendMessage(server.getPlayersConnected().size() + " players are on the server " + args[1]).queue();
            } else {
                channel.sendMessage("This server doesn't exists!").queue();
            }
        }
    }

}