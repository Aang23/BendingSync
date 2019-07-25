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
public class CommandKick implements Command {

    @Override
    public void execute(@NonNull CommandSource source, String[] args) {
        if (args.length == 1) {
            String p_name = args[0];
            Optional<Player> player = BendingSync.server.getPlayer(p_name);
            if (player.isPresent()) {
                player.get().disconnect(TextComponent.of("You've been kicked!").color(TextColor.RED));
            } else {
                source.sendMessage(TextComponent.of("The player " + p_name + " is not online!").color(TextColor.RED));
            }
        } else if (args.length >= 2) {
            String p_name = args[0];
            String message = "";
            for (int i = 1; i < args.length; i++)
                message += args[i];
            Optional<Player> player = BendingSync.server.getPlayer(p_name);
            if (player.isPresent()) {
                player.get().disconnect(LegacyComponentSerializer.INSTANCE.deserialize(message));
            } else {
                source.sendMessage(TextComponent.of("The player " + p_name + " is not online!").color(TextColor.RED));
            }
        } else {
            sendUsage(source);
        }
    }

    private void sendUsage(CommandSource source) {
        source.sendMessage(TextComponent.of("Usage : /kick <user> [message]").color(TextColor.RED));
    }
}