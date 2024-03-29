package com.aang23.bendingsync.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

/**
 * @author Aang23
 */
public class AvatarUnsetCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player spongePlayer = args.<Player>getOne("player").get();

        // AvatarCycleUtils.unsetTheAvatar(spongePlayer);

        return CommandResult.success();
    }
}