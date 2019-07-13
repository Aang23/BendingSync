package com.aang23.bendingsync.commands;

//import com.aang23.bendingsync.utils.AvatarCycleUtils;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

public class AvatarSetCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player spongePlayer = args.<Player>getOne("player").get();

        //AvatarCycleUtils.setTheAvatar(spongePlayer);

        return CommandResult.success();
    }
}