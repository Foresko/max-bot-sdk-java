package ru.max.bot.builders.attachments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ru.max.bot.Randoms;
import ru.max.botapi.model.Attachment;
import ru.max.botapi.model.AttachmentRequest;
import ru.max.botapi.model.AudioAttachmentRequest;
import ru.max.botapi.model.Button;
import ru.max.botapi.model.CallbackButton;
import ru.max.botapi.model.FileAttachmentRequest;
import ru.max.botapi.model.InlineKeyboardAttachmentRequest;
import ru.max.botapi.model.InlineKeyboardAttachmentRequestPayload;
import ru.max.botapi.model.PhotoAttachment;
import ru.max.botapi.model.PhotoAttachmentPayload;
import ru.max.botapi.model.PhotoAttachmentRequest;
import ru.max.botapi.model.PhotoAttachmentRequestPayload;
import ru.max.botapi.model.StickerAttachmentRequest;
import ru.max.botapi.model.StickerAttachmentRequestPayload;
import ru.max.botapi.model.UploadedInfo;
import ru.max.botapi.model.VideoAttachmentRequest;

import static ru.max.bot.builders.attachments.AttachmentsBuilder.audios;
import static ru.max.bot.builders.attachments.AttachmentsBuilder.files;
import static ru.max.bot.builders.attachments.AttachmentsBuilder.videos;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AttachmentsBuilderTest {
    @Test
    public void test1() {
        Button button = new CallbackButton("1", "1");
        Button button2 = new CallbackButton("2", "2");
        Button button3 = new CallbackButton("3", "3");

        String token1 = Randoms.text();
        String token2 = Randoms.text();
        String token3 = Randoms.text();
        String token4 = Randoms.text();
        String token5 = Randoms.text();
        String token6 = Randoms.text();
        String stickerCode = Randoms.text();
        List<AttachmentRequest> attachments = AttachmentsBuilder
                .photos("123", "345")
                .with(PhotosBuilder.byUrls("photoUrl"))
                .with(AttachmentsBuilder.videos("678"))
                .with(videos(new UploadedInfo().token(token5), new UploadedInfo().token(token6)))
                .with(AttachmentsBuilder.audios(token1, token2))
                .with(audios(new UploadedInfo().token(token1), new UploadedInfo().token(token2)))
                .with(AttachmentsBuilder.files(token3, token4))
                .with(files(new UploadedInfo().token(token3), new UploadedInfo().token(token4)))
                .with(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.single(button).addRow(button2, button3)))
                .with(AttachmentsBuilder.sticker(stickerCode))
                .getList();

        List<AttachmentRequest> expected = new ArrayList<>();
        expected.add(new PhotoAttachmentRequest(new PhotoAttachmentRequestPayload().token("123")));
        expected.add(new PhotoAttachmentRequest(new PhotoAttachmentRequestPayload().token("345")));
        expected.add(new PhotoAttachmentRequest(new PhotoAttachmentRequestPayload().url("photoUrl")));

        expected.add(new VideoAttachmentRequest(new UploadedInfo().token("678")));
        expected.add(new VideoAttachmentRequest(new UploadedInfo().token(token5)));
        expected.add(new VideoAttachmentRequest(new UploadedInfo().token(token6)));

        expected.add(new AudioAttachmentRequest(new UploadedInfo().token(token1)));
        expected.add(new AudioAttachmentRequest(new UploadedInfo().token(token2)));
        expected.add(new AudioAttachmentRequest(new UploadedInfo().token(token1)));
        expected.add(new AudioAttachmentRequest(new UploadedInfo().token(token2)));

        expected.add(new FileAttachmentRequest(new UploadedInfo().token(token3)));
        expected.add(new FileAttachmentRequest(new UploadedInfo().token(token4)));
        expected.add(new FileAttachmentRequest(new UploadedInfo().token(token3)));
        expected.add(new FileAttachmentRequest(new UploadedInfo().token(token4)));

        expected.add(new InlineKeyboardAttachmentRequest(new InlineKeyboardAttachmentRequestPayload(Arrays.asList(
                Collections.singletonList(button),
                Arrays.asList(button2, button3)
        ))));
        
        expected.add(new StickerAttachmentRequest(new StickerAttachmentRequestPayload(stickerCode)));

        assertThat(attachments, is(expected));
    }

    @Test
    public void test2() {
        Attachment attach1 = new PhotoAttachment(new PhotoAttachmentPayload(1L, "1", "1"));
        Attachment attach2 = new PhotoAttachment(new PhotoAttachmentPayload(2L, "2", "2"));
        Button button = new CallbackButton("1", "1");
        List<AttachmentRequest> result = AttachmentsBuilder.copyOf(Arrays.asList(attach1, attach2))
                .with(AttachmentsBuilder.inlineKeyboard(InlineKeyboardBuilder.single(button)))
                .getList();

        List<AttachmentRequest> expected = new ArrayList<>();
        expected.add(new PhotoAttachmentRequest(new PhotoAttachmentRequestPayload().token("1")));
        expected.add(new PhotoAttachmentRequest(new PhotoAttachmentRequestPayload().token("2")));
        expected.add(new InlineKeyboardAttachmentRequest(new InlineKeyboardAttachmentRequestPayload(
                Collections.singletonList(Collections.singletonList(button)))));

        assertThat(result, is(expected));
    }
}