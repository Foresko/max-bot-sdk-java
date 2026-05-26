package ru.max.bot.webhook;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.max.bot.MaxBot;
import ru.max.bot.MaxBotBase;
import ru.max.bot.exceptions.MaxBotException;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.exceptions.APIException;
import ru.max.botapi.exceptions.ClientException;
import ru.max.botapi.model.SimpleQueryResult;
import ru.max.botapi.model.Subscription;
import ru.max.botapi.model.SubscriptionRequestBody;
import ru.max.botapi.queries.GetSubscriptionsQuery;
import ru.max.botapi.queries.SubscribeQuery;
import ru.max.botapi.queries.UnsubscribeQuery;

public class WebhookBot extends MaxBotBase implements MaxBot {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final WebhookBotOptions options;
    private final AtomicBoolean running = new AtomicBoolean();

    public WebhookBot(String accessToken, Object... handlers) {
        this(accessToken, WebhookBotOptions.DEFAULT, handlers);
    }

    public WebhookBot(String accessToken, WebhookBotOptions options, Object... handlers) {
        this(MaxClient.create(accessToken), options, handlers);
    }

    public WebhookBot(MaxClient client, WebhookBotOptions options, Object... handlers) {
        super(client, handlers);
        this.options = options;
    }

    /**
     * @return true if bot started successfully
     */
    public boolean start(WebhookBotContainer container) throws MaxBotException {
        if (!running.compareAndSet(false, true)) {
            return false;
        }

        if (options.shouldRemoveOldSubscriptions()) {
            try {
                unsubscribeAll();
            } catch (APIException | ClientException e) {
                LOG.warn("Failed to remove current subscriptions", e);
            }
        }

        try {
            subscribe(container.getWebhookUrl(this));
        } catch (APIException | ClientException e) {
            throw new MaxBotException("Failed to start webhook bot", e);
        }

        return true;
    }

    /**
     * @return false if bot is not running
     */
    public boolean stop(WebhookBotContainer container) {
        return running.compareAndSet(true, false);
    }

    /**
     * Should return unique key across all bots in container. Returns access token by default.
     */
    public String getKey() {
        return getClient().getAccessToken();
    }

    public boolean isRunning() {
        return running.get();
    }

    protected void subscribe(String webhookUrl) throws APIException, ClientException {
        SubscriptionRequestBody body = new SubscriptionRequestBody(webhookUrl);
        body.updateTypes(options.getUpdateTypes());
        new SubscribeQuery(getClient(), body).execute();
        LOG.info("Bot {} registered webhook URL: {}", this, webhookUrl);
    }

    protected void unsubscribeAll() throws APIException, ClientException {
        List<Subscription> subscriptions = new GetSubscriptionsQuery(getClient()).execute().getSubscriptions();
        for (Subscription subscription : subscriptions) {
            SimpleQueryResult result = new UnsubscribeQuery(getClient(), subscription.getUrl()).execute();
            if (!result.isSuccess()) {
                LOG.warn("Failed to remove subscription {}. Reason: {}", subscription.getUrl(), result.getMessage());
            }
        }
    }
}
