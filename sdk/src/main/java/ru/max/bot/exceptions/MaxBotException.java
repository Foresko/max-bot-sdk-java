package ru.max.bot.exceptions;

public class MaxBotException extends Exception {
    public MaxBotException(Throwable cause) {
        super(cause);
    }

    public MaxBotException(String message, Exception cause) {
        super(message, cause);
    }
}
