package com.aang23.bendingsync.discord;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DiscordCommands {
    private static Map<String, DiscordCommand> COMMANDS = new HashMap<String, DiscordCommand>();
    private static final String COMMAND_PREFIX = "&";

    public static void registerCommand(DiscordCommand cmd) {
        COMMANDS.put(COMMAND_PREFIX + cmd.getCommandName(), cmd);
    }

    public static void runCommand(MessageReceivedEvent e) {
        String args[] = e.getMessage().getContentDisplay().split(" ");
        String cmd_name = args[0];

        if (!cmd_name.substring(0, 1).equals(COMMAND_PREFIX))
            return;

        if (COMMANDS.containsKey(cmd_name)) {
            DiscordCommand cmd = COMMANDS.get(cmd_name);
            if (cmd.getCommandArgsCount() <= args.length) {
                cmd.executeCommand(args, e.getChannel(), e.getAuthor());
            } else {
                e.getChannel().sendMessage("Syntax : " + cmd.getCommandSyntax()).queue();
            }
        } else if (cmd_name.equals(COMMAND_PREFIX + "help")) {
            listCommandsAndUsage(e.getChannel());
        } else {
            e.getChannel().sendMessage("Command not found! Do `" + COMMAND_PREFIX + "help` for a list").queue();
        }
    }

    public static void listCommandsAndUsage(MessageChannel ch) {
        String list = "Commands :\n" + COMMAND_PREFIX + "help : Prints this message\n";
        for (DiscordCommand cmd : COMMANDS.values())
            list += cmd.getCommandSyntax() + " : " + cmd.getCommandUsage() + "\n";
        ch.sendMessage(list).queue();
    }
}