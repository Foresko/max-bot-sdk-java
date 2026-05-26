package ru.max.bot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.max.bot.MaxBotBase;
import ru.max.bot.commands.Command;

/**
 * Indicates that an annotated method handles incoming bot command (a message started with `/command`).
 * Such methods are considered as candidates for auto-detection
 * when its containing class added as "handler" to {@link MaxBotBase MaxBotBase} or its inheritors.
 * An annotated method must have {@link ru.max.botapi.model.Message Message}
 * as the first parameter in the definition, but amount of parameters can be more than one.
 * <p>
 * There is one more way to define command: using {@link Command} and
 * {@link ru.max.bot.commands.CommandHandler}
 *
 * @see MaxBotBase
 * @see ru.max.botapi.model.Message
 * @see Command
 * @see ru.max.bot.commands.CommandHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandHandler {

    /**
     * Command name, it mustn't be empty or null.
     * Can start with '/' or without.
     *
     * @return the suggested command name
     */
    String value();

    /**
     * Specifies whether the command's arguments should be parsed.
     */
    boolean parseArgs() default true;
}
