package ru.max.bot;

import java.util.List;

import ru.max.botapi.model.Attachment;
import ru.max.botapi.model.Message;
import ru.max.botapi.model.MessageBody;

public class Mocks {
    public static Message message(String text) {
        return message(text, null);
    }

    public static Message message(String text, List<Attachment> attachments) {
        MessageBody body = new MessageBody(null, null, text, attachments);
        return new Message(null, null, body);
    }
}
