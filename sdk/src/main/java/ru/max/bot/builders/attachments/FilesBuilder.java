package ru.max.bot.builders.attachments;

import ru.max.botapi.model.AttachmentRequest;
import ru.max.botapi.model.FileAttachmentRequest;
import ru.max.botapi.model.UploadedInfo;

public class FilesBuilder extends MediaAttachmentBuilder {
    public FilesBuilder(UploadedInfo... uploadedInfos) {
        super(uploadedInfos);
    }

    public FilesBuilder(String... tokens) {
        super(tokens);
    }

    @Override
    protected AttachmentRequest toAttachRequest(UploadedInfo uploadedInfo) {
        return new FileAttachmentRequest(uploadedInfo);
    }
}
