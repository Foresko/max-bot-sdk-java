package ru.max.bot.builders.attachments;

import ru.max.botapi.model.AttachmentRequest;
import ru.max.botapi.model.AudioAttachmentRequest;
import ru.max.botapi.model.UploadedInfo;

public class AudiosBuilder extends MediaAttachmentBuilder {
    public AudiosBuilder(UploadedInfo... infos) {
        super(infos);
    }

    public AudiosBuilder(String... tokens) {
        super(tokens);
    }

    @Override
    protected AttachmentRequest toAttachRequest(UploadedInfo uploadedInfo) {
        return new AudioAttachmentRequest(uploadedInfo);
    }
}
