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

public class NoopUpdateVisitor implements Update.Visitor {
    @Override
    public void visit(MessageCreatedUpdate model) {

    }

    @Override
    public void visit(MessageCallbackUpdate model) {

    }

    @Override
    public void visit(MessageEditedUpdate model) {

    }

    @Override
    public void visit(MessageRemovedUpdate model) {

    }

    @Override
    public void visit(BotAddedToChatUpdate model) {

    }

    @Override
    public void visit(BotRemovedFromChatUpdate model) {

    }

    @Override
    public void visit(UserAddedToChatUpdate model) {

    }

    @Override
    public void visit(UserRemovedFromChatUpdate model) {

    }

    @Override
    public void visit(BotStartedUpdate model) {

    }

    @Override
    public void visit(ChatTitleChangedUpdate model) {

    }

    @Override
    public void visit(MessageChatCreatedUpdate model) {

    }

    @Override
    public void visitDefault(Update model) {

    }
}
