package com.aang23.bendingsync.discord;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * Interface to be used by all discord commands
 * 
 * @author Aang23
 */
public interface DiscordCommand {
    /**
     * Returns this command's name without the prefix
     * 
     * @return
     */
    public String getCommandName();

    /**
     * Returns the required syntax
     * 
     * @return
     */
    public String getCommandSyntax();

    /**
     * Returns the recommended usage for that command
     * 
     * @return
     */
    public String getCommandUsage();

    /**
     * Returns the minimym number of arguments this commands requires
     * 
     * @return
     */
    public int getCommandArgsCount();

    /**
     * Execute that command
     * 
     * @param args
     * @param channel
     * @param user
     */
    public void executeCommand(String args[], MessageChannel channel, User user);
}