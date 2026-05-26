package ru.max.bot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.max.bot.MaxBotBase;

/**
 * Indicates that an annotated method handles incoming updates.
 * Such methods are considered as candidates for auto-detection
 * when its containing class added as "handler" to {@link MaxBotBase MaxBotBase} or its inheritors.
 * An annotated method must have one parameter with concrete implementation of {@link ru.max.botapi.model.Update Update}
 * in the definition.
 *
 * @see MaxBotBase
 * @see ru.max.botapi.model.Update
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpdateHandler {
}
