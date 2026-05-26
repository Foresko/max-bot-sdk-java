package ru.max.bot.commands;

import ru.max.botapi.model.Message;

public interface CommandHandler {
    CommandHandler NOOP = (message, commandLine) -> {

    };

    /**
     * Method to handle incoming bot command.
     * @param message message from {@link ru.max.botapi.model.MessageCreatedUpdate MessageCreatedUpdate}
     * @param commandLine parsed command
     */
    void execute(Message message, CommandLine commandLine);
}
