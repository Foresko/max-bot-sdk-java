package ru.max.bot.builders;

import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.botapi.model.LinkedMessage;
import ru.max.botapi.model.Message;
import ru.max.botapi.model.MessageBody;
import ru.max.botapi.model.MessageLinkType;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.NewMessageLink;
import ru.max.botapi.model.TextFormat;

public class NewMessageBodyBuilder {
    private String text;
    private AttachmentsBuilder attachments;
    private NewMessageLink link;
    private TextFormat format;

    private NewMessageBodyBuilder(String text, AttachmentsBuilder attachments, NewMessageLink link) {
        this.text = text;
        this.attachments = attachments;
        this.link = link;
    }

    public static NewMessageBodyBuilder copyOf(Message message) {
        MessageBody messageBody = message.getBody();
        return ofText(messageBody.getText())
                .withAttachments(AttachmentsBuilder.copyOf(messageBody.getAttachments()))
                .withLink(message.getLink());
    }

    public static NewMessageBodyBuilder ofText(String text) {
        return new NewMessageBodyBuilder(text, null, null);
    }

    public static NewMessageBodyBuilder ofText(String text, TextFormat format) {
        NewMessageBodyBuilder builder = new NewMessageBodyBuilder(text, null, null);
        builder.format = format;
        return builder;
    }

    public static NewMessageBodyBuilder ofAttachments(AttachmentsBuilder attachmentsBuilder) {
        return new NewMessageBodyBuilder(null, attachmentsBuilder, null);
    }

    public static NewMessageBodyBuilder forward(String messageId) {
        return new NewMessageBodyBuilder(null, null, new NewMessageLink(MessageLinkType.FORWARD, messageId));
    }

    public static NewMessageBodyBuilder reply(String messageId) {
        return new NewMessageBodyBuilder(null, null, new NewMessageLink(MessageLinkType.REPLY, messageId));
    }

    public NewMessageBodyBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public NewMessageBodyBuilder withText(String text, TextFormat format) {
        withText(text);
        this.format = format;
        return this;
    }

    public NewMessageBodyBuilder withAttachments(AttachmentsBuilder attachments) {
        this.attachments = attachments;
        return this;
    }

    public NewMessageBodyBuilder withReply(String messageId) {
        this.link = new NewMessageLink(MessageLinkType.REPLY, messageId);
        return this;
    }

    public NewMessageBodyBuilder withForward(String messageId) {
        this.link = new NewMessageLink(MessageLinkType.FORWARD, messageId);
        return this;
    }

    public NewMessageBodyBuilder withLink(LinkedMessage linkedMessage) {
        if (linkedMessage == null) {
            return this;
        }

        switch (linkedMessage.getType()) {
            case FORWARD:
                return withForward(linkedMessage.getMessage().getMid());
            case REPLY:
                return withReply(linkedMessage.getMessage().getMid());
        }

        return this;
    }

    public NewMessageBody build() {
        return new NewMessageBody(text, attachments == null ? null : attachments.getList(), link)
                .format(format);
    }

    public String getText() {
        return text;
    }

    public AttachmentsBuilder getAttachments() {
        return attachments;
    }

    public NewMessageLink getLink() {
        return link;
    }
}
