package com.aang23.bendingsync.commands;

import com.aang23.bendingsync.mysql.MysqlHandler;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;

import org.checkerframework.checker.nullness.qual.NonNull;

import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

/**
 * @author Aang23
 */
public class CommandUnban implements Command {

    @Override
    public void execute(@NonNull CommandSource source, String[] args) {

        if (source.hasPermission("bendingsync.command.unban")) {

            if (args.length == 2) {
                String p_name = args[1];
                String type = args[0];

                if (!(type.equals("uuid") || type.equals("name"))) {
                    sendUsage(source);
                    return;
                }

                boolean isUuidOrName = type.equals("uuid");

                boolean isBanned = isUuidOrName ? MysqlHandler.isPlayerBannedUuid(p_name)
                        : MysqlHandler.isPlayerBannedName(p_name);

                if (isBanned) {
                    if (isUuidOrName) {
                        MysqlHandler.setUserunbanUuid(p_name);
                    } else {
                        MysqlHandler.setUserunbanUsername(p_name);
                    }
                    source.sendMessage(
                            TextComponent.of("The player " + p_name + " has been unbanned!").color(TextColor.GREEN));
                } else {
                    source.sendMessage(
                            TextComponent.of("The player " + p_name + " is not banned!").color(TextColor.RED));
                }
            } else {
                sendUsage(source);
            }
        } else {
            source.sendMessage(
                    TextComponent.of("You don't have the permission to use that command!").color(TextColor.RED));
        }
    }

    private void sendUsage(CommandSource source) {
        source.sendMessage(TextComponent.of("Usage : /unban <uuid|name> <username|uuid>").color(TextColor.RED));
    }
}