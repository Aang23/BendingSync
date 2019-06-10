package com.aang23.bendingsync.commands;

import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandRegistrar {
    public static void registerCommands(){
        CommandSpec bendingsyncResetCommand = CommandSpec.builder()
                .description(Text.of("Reset data of an user"))
                .permission("bendingsync.command.reset")
                .arguments(GenericArguments.player(Text.of("player")))
                .executor(new ResetCommand())
                .build();

        CommandSpec bendingsyncCommand = CommandSpec.builder()
                .description(Text.of("BendingSync command"))
                .permission("bendingsync.command.main")
                .child(bendingsyncResetCommand, "reset")
                .build();
    }
}