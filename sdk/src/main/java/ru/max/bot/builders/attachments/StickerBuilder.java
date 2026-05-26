package ru.max.bot.builders.attachments;

import java.util.stream.Stream;

import ru.max.botapi.model.AttachmentRequest;
import ru.max.botapi.model.StickerAttachmentRequest;
import ru.max.botapi.model.StickerAttachmentRequestPayload;

import static java.util.Objects.requireNonNull;

public class StickerBuilder implements AttachmentsBuilder {
    private final String stickerCode;

    public StickerBuilder(String stickerCode) {
        this.stickerCode = requireNonNull(stickerCode, "stickerCode is null");
    }

    @Override
    public Stream<AttachmentRequest> build() {
        return Stream.of(new StickerAttachmentRequest(new StickerAttachmentRequestPayload(stickerCode)));
    }
}
