package com.aang23.bendingsync.commands;

import java.util.Optional;

import com.aang23.bendingsync.BendingSync;
import com.aang23.bendingsync.mysql.MysqlHandler;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import org.checkerframework.checker.nullness.qual.NonNull;

import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

/**
 * @author Aang23
 */
public class CommandBan implements Command {

    @Override
    public void execute(@NonNull CommandSource source, String[] args) {

        if (source.hasPermission("bendingsync.command.ban")) {

            if (args.length >= 2) {
                String p_name = args[0];
                String message = "";
                for (int i = 1; i < args.length; i++)
                    message += args[i] + " ";
                Optional<Player> player = BendingSync.server.getPlayer(p_name);
                if (player.isPresent()) {
                    MysqlHandler.setUserban(player.get().getGameProfile().getId().toString(), p_name, message);
                    player.get().disconnect(LegacyComponentSerializer.INSTANCE.deserialize(message, '&'));
                    source.sendMessage(
                            TextComponent.of("The player " + p_name + " has been banned!").color(TextColor.GREEN));
                } else {
                    source.sendMessage(
                            TextComponent.of("The player " + p_name + " is not online!").color(TextColor.RED));
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
        source.sendMessage(TextComponent.of("Usage : /ban <user> <reason>").color(TextColor.RED));
    }
}