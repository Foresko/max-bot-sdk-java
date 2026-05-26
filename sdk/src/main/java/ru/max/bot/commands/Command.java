package ru.max.bot.commands;

public interface Command {
    /**
     * @return command name without '/'
     */
    String getKey();
}
