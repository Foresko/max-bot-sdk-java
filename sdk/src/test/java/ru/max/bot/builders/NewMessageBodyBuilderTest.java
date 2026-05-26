package ru.max.bot.builders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ru.max.bot.builders.attachments.AttachmentsBuilder;
import ru.max.bot.builders.attachments.InlineKeyboardBuilder;
import ru.max.botapi.model.AttachmentRequest;
import ru.max.botapi.model.Button;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.InlineKeyboardAttachmentRequest;
import ru.max.botapi.model.InlineKeyboardAttachmentRequestPayload;
import ru.max.botapi.model.MessageLinkType;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.NewMessageLink;
import ru.max.botapi.model.UploadedInfo;
import ru.max.botapi.model.VideoAttachmentRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class NewMessageBodyBuilderTest {
    private Button button1 = new CallbackButton("1", "1");
    private Button button2 = new CallbackButton("2", "2");
    private AttachmentsBuilder attachments = AttachmentsBuilder
            .videos("1", "2", "3")
            .with(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.singleRow(button1, button2)));

    private List<AttachmentRequest> expectedAttachments = Arrays.asList(
            new VideoAttachmentRequest(new UploadedInfo().token("1")),
            new VideoAttachmentRequest(new UploadedInfo().token("2")),
            new VideoAttachmentRequest(new UploadedInfo().token("3")),
            new InlineKeyboardAttachmentRequest(new InlineKeyboardAttachmentRequestPayload(
                    Collections.singletonList(Arrays.asList(button1, button2))))
    );

    @Test
    public void test1() {
        String text = "test";
        NewMessageBody newMessageBody = NewMessageBodyBuilder.ofText(text)
                .withAttachments(attachments)
                .build();

        assertThat(newMessageBody.getText(), is(text));
        assertThat(newMessageBody.getLink(), is(nullValue()));
        assertThat(newMessageBody.getAttachments(), is(expectedAttachments));
    }

    @Test
    public void test2() {
        String text = "text2";
        NewMessageBody body = NewMessageBodyBuilder.ofAttachments(attachments)
                .withText(text)
                .build();

        assertThat(body.getText(), is(text));
        assertThat(body.getLink(), is(nullValue()));
        assertThat(body.getAttachments(), is(expectedAttachments));
    }

    @Test
    public void test3() {
        String text = "text2";
        String messageId = "messageId";
        NewMessageBody body = NewMessageBodyBuilder.forward(messageId)
                .withAttachments(attachments)
                .withText(text)
                .build();

        assertThat(body.getText(), is(text));
        assertThat(body.getLink(), is(new NewMessageLink(MessageLinkType.FORWARD, messageId)));
        assertThat(body.getAttachments(), is(expectedAttachments));
    }

    @Test
    public void test4() {
        String text = "text2";
        String messageId = "messageId";
        NewMessageBody body = NewMessageBodyBuilder.reply(messageId)
                .withAttachments(attachments)
                .withText(text)
                .build();

        assertThat(body.getText(), is(text));
        assertThat(body.getLink(), is(new NewMessageLink(MessageLinkType.REPLY, messageId)));
        assertThat(body.getAttachments(), is(expectedAttachments));
    }

    @Test
    public void test5() {
        String messageId = "messageId";
        NewMessageBody body = NewMessageBodyBuilder.ofAttachments(attachments)
                .withReply(messageId)
                .build();

        assertThat(body.getLink(), is(new NewMessageLink(MessageLinkType.REPLY, messageId)));
        assertThat(body.getAttachments(), is(expectedAttachments));
    }

    @Test
    public void test6() {
        String messageId = "messageId";
        NewMessageBody body = NewMessageBodyBuilder.ofAttachments(attachments)
                .withForward(messageId)
                .build();

        assertThat(body.getLink(), is(new NewMessageLink(MessageLinkType.FORWARD, messageId)));
        assertThat(body.getAttachments(), is(expectedAttachments));
    }
}