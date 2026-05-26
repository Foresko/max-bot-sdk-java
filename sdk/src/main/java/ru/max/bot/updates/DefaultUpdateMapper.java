package ru.max.bot.updates;

import ru.max.botapi.model.BotAddedToChatUpdate;
import ru.max.botapi.model.BotRemovedFromChatUpdate;
import ru.max.botapi.model.BotStartedUpdate;
import ru.max.botapi.model.ChatTitleChangedUpdate;
import ru.max.botapi.model.MessageCallbackUpdate;
import ru.max.botapi.model.MessageChatCreatedUpdate;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.MessageEditedUpdate;
import ru.max.botapi.model.MessageRemovedUpdate;
import ru.max.botapi.model.Update;
import ru.max.botapi.model.UserAddedToChatUpdate;
import ru.max.botapi.model.UserRemovedFromChatUpdate;

public abstract class DefaultUpdateMapper<T> implements Update.Mapper<T> {
    @Override
    public T map(MessageCreatedUpdate model) {
        return mapDefault(model);
    }

    @Override
    public T map(MessageCallbackUpdate model) {
        return mapDefault(model);
    }

    @Override
    public T map(MessageEditedUpdate model) {
        return mapDefault(model);
    }

    @Override
    public T map(MessageRemovedUpdate model) {
        return mapDefault(model);
    }

    @Override
    public T map(BotAddedToChatUpdate model) {
        return mapDefault(model);
    }

    @Override
    public T map(BotRemovedFromChatUpdate model) {
        return mapDefault(model);
    }

    @Override
    public T map(UserAddedToChatUpdate model) {
        return mapDefault(model);
    }

    @Override
    public T map(UserRemovedFromChatUpdate model) {
        return mapDefault(model);
    }

    @Override
    public T map(BotStartedUpdate model) {
        return mapDefault(model);
    }

    @Override
    public T map(ChatTitleChangedUpdate model) {
        return mapDefault(model);
    }

    @Override
    public T map(MessageChatCreatedUpdate model) {
        return mapDefault(model);
    }
}
