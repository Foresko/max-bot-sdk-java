package ru.max.bot.builders.attachments;

import ru.max.botapi.model.AttachmentRequest;
import ru.max.botapi.model.UploadedInfo;
import ru.max.botapi.model.VideoAttachmentRequest;

public class VideosBuilder extends MediaAttachmentBuilder {
    public VideosBuilder(UploadedInfo... uploadedInfos) {
        super(uploadedInfos);
    }

    public VideosBuilder(String... tokens) {
        super(tokens);
    }

    @Override
    protected AttachmentRequest toAttachRequest(UploadedInfo uploadedInfo) {
        return new VideoAttachmentRequest(uploadedInfo);
    }

}
