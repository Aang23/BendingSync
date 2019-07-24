package com.aang23.bendingsync.commands;

import com.aang23.bendingsync.BendingSync;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * Class used for registering commands
 * 
 * @author Aang23
 */
public class CommandRegistrar {
    public static void registerCommands() {
        // @formatter:off
        CommandSpec bendingsyncResetCommand = CommandSpec.builder()
                .description(Text.of("Reset data of an user"))
                .permission("bendingsync.command.reset")
                .arguments(GenericArguments.player(Text.of("player")))
                .executor(new ResetCommand())
                .build();

        CommandSpec setAvatarCommand = CommandSpec.builder()
                .description(Text.of("set an avatar"))
                .permission("bendingsync.command.avatar.set")
                .arguments(GenericArguments.player(Text.of("player")))
                .executor(new AvatarSetCommand())
                .build();

        CommandSpec unsetAvatarCommand = CommandSpec.builder()
                .description(Text.of("unset an avatar"))
                .permission("bendingsync.command.avatar.unset")
                .arguments(GenericArguments.player(Text.of("player")))
                .executor(new AvatarUnsetCommand())
                .build();

        CommandSpec avatarCommand = CommandSpec.builder()
                .description(Text.of("avatar subcommand"))
                .permission("bendingsync.command.avatar")
                .child(setAvatarCommand, "set")
                .child(unsetAvatarCommand, "unset")
                .build();

        CommandSpec bendingsyncCommand = CommandSpec.builder()
                .description(Text.of("BendingSync command"))
                .permission("bendingsync.command.main")
                .child(bendingsyncResetCommand, "reset")
                .child(avatarCommand, "avatar")
                .build();
        // @formatter:on

        Sponge.getCommandManager().register(BendingSync.INSTANCE, bendingsyncCommand, "bendingsync");
    }
}