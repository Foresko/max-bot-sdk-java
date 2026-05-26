package ru.max.bot.builders.attachments;

import java.util.Arrays;
import java.util.Objects;

import ru.max.botapi.model.PhotoAttachmentRequest;
import ru.max.botapi.model.PhotoAttachmentRequestPayload;

public class PhotosBuilder {
    public static AttachmentsBuilder byTokens(String... tokens) {
        return () -> Arrays.stream(Objects.requireNonNull(tokens, "tokens"))
                .map(token -> new PhotoAttachmentRequest(new PhotoAttachmentRequestPayload().token(token)));
    }

    public static AttachmentsBuilder byUrls(String... urls) {
        return () -> Arrays.stream(urls)
                .map(url -> new PhotoAttachmentRequest(new PhotoAttachmentRequestPayload().url(url)));
    }
}
