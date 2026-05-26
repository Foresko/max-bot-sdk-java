package ru.max.bot.webhook.jetty;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.max.bot.exceptions.BotNotFoundException;
import ru.max.bot.exceptions.WebhookException;
import ru.max.bot.webhook.WebhookBotContainer;

class WebhookDispatcher extends Handler.Abstract {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private WebhookBotContainer botContainer;

    WebhookDispatcher(WebhookBotContainer botContainer) {
        this.botContainer = botContainer;
    }

    @Override
    public boolean handle(Request request, Response response, Callback callback) throws IOException {
        String webhookResponse;
        try {
            webhookResponse = botContainer.handleRequest(
                    request.getHttpURI().getPath(),
                    request.getMethod(),
                    Request.asInputStream(request)
            );
        } catch (BotNotFoundException e) {
            LOG.warn(e.getMessage());
            Response.writeError(request, response, callback, e.getErrorCode(), e.getMessage());
            return true;
        } catch (WebhookException e) {
            String errorId = Long.toHexString(ThreadLocalRandom.current().nextLong());
            LOG.error("Error happend while handling request: {}. Error ID: {}", request, errorId, e);
            Response.writeError(request, response, callback, e.getErrorCode(), "Error ID: " + errorId);
            return true;
        }

        response.setStatus(200);
        if (webhookResponse == null) {
            response.write(true, ByteBuffer.allocate(0), callback);
            return true;
        }

        response.write(true, ByteBuffer.wrap((webhookResponse + "\n").getBytes(StandardCharsets.UTF_8)), callback);
        return true;
    }
}
