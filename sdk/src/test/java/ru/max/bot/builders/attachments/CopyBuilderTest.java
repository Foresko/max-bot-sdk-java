package ru.max.bot.builders.attachments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import ru.max.bot.Randoms;
import ru.max.botapi.model.Attachment;
import ru.max.botapi.model.AttachmentRequest;
import ru.max.botapi.model.AudioAttachment;
import ru.max.botapi.model.AudioAttachmentRequest;
import ru.max.botapi.model.Button;
import ru.max.botapi.model.ContactAttachment;
import ru.max.botapi.model.ContactAttachmentPayload;
import ru.max.botapi.model.ContactAttachmentRequest;
import ru.max.botapi.model.ContactAttachmentRequestPayload;
import ru.max.botapi.model.FileAttachment;
import ru.max.botapi.model.FileAttachmentPayload;
import ru.max.botapi.model.FileAttachmentRequest;
import ru.max.botapi.model.InlineKeyboardAttachment;
import ru.max.botapi.model.InlineKeyboardAttachmentRequest;
import ru.max.botapi.model.InlineKeyboardAttachmentRequestPayload;
import ru.max.botapi.model.Keyboard;
import ru.max.botapi.model.LinkButton;
import ru.max.botapi.model.LocationAttachment;
import ru.max.botapi.model.LocationAttachmentRequest;
import ru.max.botapi.model.MediaAttachmentPayload;
import ru.max.botapi.model.PhotoAttachment;
import ru.max.botapi.model.PhotoAttachmentPayload;
import ru.max.botapi.model.PhotoAttachmentRequest;
import ru.max.botapi.model.PhotoAttachmentRequestPayload;
import ru.max.botapi.model.ShareAttachment;
import ru.max.botapi.model.ShareAttachmentPayload;
import ru.max.botapi.model.ShareAttachmentRequest;
import ru.max.botapi.model.StickerAttachment;
import ru.max.botapi.model.StickerAttachmentPayload;
import ru.max.botapi.model.StickerAttachmentRequest;
import ru.max.botapi.model.StickerAttachmentRequestPayload;
import ru.max.botapi.model.UploadedInfo;
import ru.max.botapi.model.User;
import ru.max.botapi.model.VideoAttachment;
import ru.max.botapi.model.VideoAttachmentRequest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CopyBuilderTest {
    @Test
    public void test0() {
        // unknown attachment
        List<AttachmentRequest> result = new CopyBuilder(new Attachment()).getList();
        assertThat(result, is(Collections.emptyList()));
    }

    @Test
    public void test1() {
        String token = Randoms.text();
        List<AttachmentRequest> result = new CopyBuilder(
                new PhotoAttachment(new PhotoAttachmentPayload(123L, token, "url"))).getList();

        assertThat(result, is(Collections.singletonList(
                new PhotoAttachmentRequest(new PhotoAttachmentRequestPayload().token(token)))));
    }

    @Test
    public void test2() {
        String token = Randoms.text();
        List<AttachmentRequest> result = new CopyBuilder(
                new VideoAttachment(new MediaAttachmentPayload(token, "url"))).build().collect(Collectors.toList());;

        assertThat(result, is(Collections.singletonList(
                new VideoAttachmentRequest(new UploadedInfo().token(token)))));
    }

    @Test
    public void test3() {
        String token = Randoms.text();
        List<AttachmentRequest> result = new CopyBuilder(
                new AudioAttachment(new MediaAttachmentPayload(token, "url"))).build().collect(Collectors.toList());;

        assertThat(result, is(Collections.singletonList(
                new AudioAttachmentRequest(new UploadedInfo().token(token)))));
    }

    @Test
    public void test4() {
        String token = Randoms.text();
        List<AttachmentRequest> result = new CopyBuilder(
                new FileAttachment(new FileAttachmentPayload(token, "url"), "filename", 1L)).getList();

        assertThat(result, is(Collections.singletonList(
                new FileAttachmentRequest(new UploadedInfo().token(token)))));
    }

    @Test
    public void test5() {
        String code = Randoms.text();
        List<AttachmentRequest> result = new CopyBuilder(
                new StickerAttachment(new StickerAttachmentPayload(code, "url"), 1, 2)).getList();

        assertThat(result, is(Collections.singletonList(
                new StickerAttachmentRequest(new StickerAttachmentRequestPayload(code)))));
    }

    @Test
    public void test6() {
        User user = Randoms.randomUser();
        List<AttachmentRequest> result = new CopyBuilder(
                new ContactAttachment(new ContactAttachmentPayload().maxInfo(user))).getList();

        assertThat(result, is(Collections.singletonList(
                new ContactAttachmentRequest(
                        new ContactAttachmentRequestPayload(user.getName()).contactId(user.getUserId())))));
    }

    @Test
    public void test7() {
        List<List<Button>> buttons = Collections.singletonList(
                Collections.singletonList(new LinkButton(Randoms.text(), Randoms.text())));

        Keyboard keyboard = new Keyboard(buttons);
        List<AttachmentRequest> result = new CopyBuilder(new InlineKeyboardAttachment(keyboard)).getList();

        assertThat(result, is(Collections.singletonList(
                new InlineKeyboardAttachmentRequest(new InlineKeyboardAttachmentRequestPayload(buttons)))));
    }

    @Test
    public void test8() {
        String url = Randoms.text();
        String token = Randoms.text();
        ShareAttachmentPayload payload = new ShareAttachmentPayload().url(url).token(token);
        List<AttachmentRequest> result = new CopyBuilder(new ShareAttachment(payload)).getList();
        assertThat(result, is(Collections.singletonList(new ShareAttachmentRequest(payload))));
    }

    @Test
    public void test9() {
        double latitude = Randoms.randomDouble();
        double longitude = Randoms.randomDouble();
        List<AttachmentRequest> result = new CopyBuilder(new LocationAttachment(latitude, longitude)).getList();
        assertThat(result, is(Collections.singletonList(new LocationAttachmentRequest(latitude, longitude))));
    }
}