package com.aang23.bendingsync.discord;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public interface DiscordCommand {
    public String getCommandName();
    public String getCommandSyntax();
    public String getCommandUsage();
    public int getCommandArgsCount();
    public void executeCommand(String args[], MessageChannel channel, User user);
}