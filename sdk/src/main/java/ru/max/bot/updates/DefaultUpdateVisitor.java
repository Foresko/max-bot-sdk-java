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

public abstract class DefaultUpdateVisitor implements Update.Visitor {
    @Override
    public void visit(MessageCreatedUpdate model) {
        visitDefault(model);
    }

    @Override
    public void visit(MessageCallbackUpdate model) {
        visitDefault(model);
    }

    @Override
    public void visit(MessageEditedUpdate model) {
        visitDefault(model);
    }

    @Override
    public void visit(MessageRemovedUpdate model) {
        visitDefault(model);
    }

    @Override
    public void visit(BotAddedToChatUpdate model) {
        visitDefault(model);
    }

    @Override
    public void visit(BotRemovedFromChatUpdate model) {
        visitDefault(model);
    }

    @Override
    public void visit(UserAddedToChatUpdate model) {
        visitDefault(model);
    }

    @Override
    public void visit(UserRemovedFromChatUpdate model) {
        visitDefault(model);
    }

    @Override
    public void visit(BotStartedUpdate model) {
        visitDefault(model);
    }

    @Override
    public void visit(ChatTitleChangedUpdate model) {
        visitDefault(model);
    }

    @Override
    public void visit(MessageChatCreatedUpdate model) {
        visitDefault(model);
    }
}
