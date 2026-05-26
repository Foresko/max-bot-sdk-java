package ru.max.bot;

import org.jetbrains.annotations.Nullable;

import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.Update;

public interface MaxBot {
    MaxClient getClient();

    @Nullable
    Object onUpdate(Update update);
}
