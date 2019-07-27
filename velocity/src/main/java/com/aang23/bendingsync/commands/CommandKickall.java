package com.aang23.bendingsync.commands;

import java.util.Optional;

import com.aang23.bendingsync.BendingSync;
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
public class CommandKickall implements Command {

    @Override
    public void execute(@NonNull CommandSource source, String[] args) {

        if (source.hasPermission("bendingsync.command.kickall")) {

            if (args.length == 0) {
                if (BendingSync.server.getAllPlayers().size() > 0) {
                    BendingSync.server.getAllPlayers().forEach(player -> processPlayerNormal(player, source));
                } else {
                    source.sendMessage(TextComponent.of("No player online!").color(TextColor.RED));
                }
            } else if (args.length >= 1) {
                String message = "";
                for (int i = 0; i < args.length; i++)
                    message += args[i] + " ";
                final String msg = message;
                if (BendingSync.server.getAllPlayers().size() > 0) {
                    BendingSync.server.getAllPlayers().forEach(player -> processPlayerMessage(player, source, msg));
                } else {
                    source.sendMessage(TextComponent.of("No player online!").color(TextColor.RED));
                }
            } else {
                sendUsage(source);
            }
        } else {
            source.sendMessage(
                    TextComponent.of("You don't have the permission to use that command!").color(TextColor.RED));
        }
    }

    private void processPlayerNormal(Player current, CommandSource source) {
        if (source instanceof Player) {
            Player psource = (Player) source;
            // Don't kick the user who ran the command
            if (!psource.getUniqueId().equals(current.getUniqueId())) {
                current.disconnect(TextComponent.of("You've been kicked!").color(TextColor.RED));
            }
        } else {
            current.disconnect(TextComponent.of("You've been kicked!").color(TextColor.RED));
        }
    }

    private void processPlayerMessage(Player current, CommandSource source, String message) {
        if (source instanceof Player) {
            Player psource = (Player) source;
            // Don't kick the user who ran the command
            if (!psource.getUniqueId().equals(current.getUniqueId())) {
                current.disconnect(LegacyComponentSerializer.INSTANCE.deserialize(message, '&'));
            }
        } else {
            current.disconnect(LegacyComponentSerializer.INSTANCE.deserialize(message, '&'));
        }
    }

    private void sendUsage(CommandSource source) {
        source.sendMessage(TextComponent.of("Usage : /kickall [message]").color(TextColor.RED));
    }
}