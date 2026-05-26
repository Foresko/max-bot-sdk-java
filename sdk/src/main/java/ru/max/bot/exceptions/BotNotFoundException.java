package ru.max.bot.exceptions;

public class BotNotFoundException extends WebhookException {
    public BotNotFoundException(String message) {
        super(404, message);
    }
}
