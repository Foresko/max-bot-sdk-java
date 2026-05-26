package ru.max.bot.builders.attachments;

import ru.max.botapi.model.Attachment;
import ru.max.botapi.model.AttachmentRequest;
import ru.max.botapi.model.AudioAttachment;
import ru.max.botapi.model.AudioAttachmentRequest;
import ru.max.botapi.model.ContactAttachment;
import ru.max.botapi.model.ContactAttachmentRequest;
import ru.max.botapi.model.ContactAttachmentRequestPayload;
import ru.max.botapi.model.DataAttachment;
import ru.max.botapi.model.FileAttachment;
import ru.max.botapi.model.FileAttachmentRequest;
import ru.max.botapi.model.InlineKeyboardAttachment;
import ru.max.botapi.model.LocationAttachment;
import ru.max.botapi.model.LocationAttachmentRequest;
import ru.max.botapi.model.PhotoAttachment;
import ru.max.botapi.model.PhotoAttachmentRequest;
import ru.max.botapi.model.PhotoAttachmentRequestPayload;
import ru.max.botapi.model.ReplyKeyboardAttachment;
import ru.max.botapi.model.ShareAttachment;
import ru.max.botapi.model.ShareAttachmentPayload;
import ru.max.botapi.model.ShareAttachmentRequest;
import ru.max.botapi.model.StickerAttachment;
import ru.max.botapi.model.StickerAttachmentRequest;
import ru.max.botapi.model.StickerAttachmentRequestPayload;
import ru.max.botapi.model.UploadedInfo;
import ru.max.botapi.model.User;
import ru.max.botapi.model.VideoAttachment;
import ru.max.botapi.model.VideoAttachmentRequest;

import java.util.stream.Stream;

/**
 * Creates {@link AttachmentRequest} from existing {@link Attachment}
 */
public class CopyBuilder implements AttachmentsBuilder, Attachment.Mapper<AttachmentRequest> {
    private final Attachment attachment;

    public CopyBuilder(Attachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public AttachmentRequest map(PhotoAttachment model) {
        String token = model.getPayload().getToken();
        return new PhotoAttachmentRequest(new PhotoAttachmentRequestPayload().token(token));
    }

    @Override
    public AttachmentRequest map(VideoAttachment model) {
        return new VideoAttachmentRequest(new UploadedInfo().token(model.getPayload().getToken()));
    }

    @Override
    public AttachmentRequest map(AudioAttachment model) {
        return new AudioAttachmentRequest(new UploadedInfo().token(model.getPayload().getToken()));
    }

    @Override
    public AttachmentRequest map(FileAttachment model) {
        return new FileAttachmentRequest(new UploadedInfo().token(model.getPayload().getToken()));
    }

    @Override
    public AttachmentRequest map(StickerAttachment model) {
        return new StickerAttachmentRequest(new StickerAttachmentRequestPayload(model.getPayload().getCode()));
    }

    @Override
    public AttachmentRequest map(ContactAttachment model) {
        User maxInfo = model.getPayload().getMaxInfo();

        ContactAttachmentRequestPayload payload;
        if (maxInfo != null) {
            payload = new ContactAttachmentRequestPayload(maxInfo.getName());
            payload.setContactId(maxInfo.getUserId());
        } else {
            payload = new ContactAttachmentRequestPayload(null);
        }
        payload.setVcfInfo(model.getPayload().getVcfInfo());

        return new ContactAttachmentRequest(payload);
    }

    @Override
    public AttachmentRequest map(InlineKeyboardAttachment model) {
        return InlineKeyboardBuilder.layout(model.getPayload().getButtons()).build();
    }

    @Override
    public AttachmentRequest map(ShareAttachment model) {
        return new ShareAttachmentRequest(new ShareAttachmentPayload()
                .token(model.getPayload().getToken())
                .url(model.getPayload().getUrl()));
    }

    @Override
    public AttachmentRequest map(LocationAttachment model) {
        return new LocationAttachmentRequest(model.getLatitude(), model.getLongitude());
    }

    @Override
    public AttachmentRequest map(ReplyKeyboardAttachment model) {
        return null; // TODO
    }

    @Override
    public AttachmentRequest map(DataAttachment model) {
        return null; // TODO
    }

    @Override
    public AttachmentRequest mapDefault(Attachment model) {
        return null;
    }

    @Override
    public Stream<AttachmentRequest> build() {
        AttachmentRequest request = attachment.map(this);
        return request == null ? Stream.empty() : Stream.of(request);
    }
}
