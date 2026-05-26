package ru.max.bot.webhook;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ru.max.bot.Randoms;
import ru.max.bot.exceptions.WebhookException;
import ru.max.bot.webhook.jetty.JettyWebhookBotContainer;
import ru.max.botapi.client.ClientResponse;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.client.MaxSerializer;
import ru.max.botapi.client.MaxTransportClient;
import ru.max.botapi.client.impl.JacksonSerializer;
import ru.max.botapi.client.impl.OkHttpTransportClient;
import ru.max.botapi.model.Update;
import ru.max.botapi.queries.SubscribeQuery;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebhookBotTest {
    @Mock
    private MaxClient client;
    private TestBot bot;
    private TestBot bot2;
    private JettyWebhookBotContainer container;
    private MaxTransportClient httpClient;
    private MaxSerializer serializer;

    @Before
    public void setUp() throws Exception {
        when(client.newCall(isA(SubscribeQuery.class))).thenReturn(CompletableFuture.completedFuture(null));

        container = spy(new JettyWebhookBotContainer("0.0.0.0", 12345));
        bot = new TestBot(client, "testbot");
        bot2 = new TestBot(client, "testbot2");
        httpClient = new OkHttpTransportClient();
        serializer = new JacksonSerializer();
        when(client.getSerializer()).thenReturn(serializer);

        container.register(bot);
        container.register(bot2);
        container.start();
    }

    @After
    public void tearDown() throws Exception {
        container.stop();
        container.join();
    }

    @Test
    public void shouldReceiveWebhooks() throws Exception {
        List<Update> updates = Stream.generate(Randoms::randomUpdate).limit(100).distinct().collect(
                Collectors.toList());
        List<Update> updates2 = Stream.generate(Randoms::randomUpdate).limit(100).distinct().collect(
                Collectors.toList());
        List<Future<ClientResponse>> responses = new ArrayList<>(updates.size());
        List<Future<ClientResponse>> responses2 = new ArrayList<>(updates.size());
        Set<Update> sentUpdates = new HashSet<>();
        Set<Update> sentUpdates2 = new HashSet<>();
        while (updates.size() > 0) {
            Update update = updates.remove(Randoms.randomInt(updates.size()));
            Update update2 = updates2.remove(Randoms.randomInt(updates2.size()));
            byte[] bytes = serializer.serialize(update);
            byte[] bytes2 = serializer.serialize(update2);
            String url = "http://0.0.0.0:12345/testbot";
            String url2 = "http://0.0.0.0:12345/testbot2";
            responses.add(httpClient.post(url, bytes));
            responses2.add(httpClient.post(url2, bytes2));
            sentUpdates.add(update);
            sentUpdates2.add(update2);
        }

        for (Future<ClientResponse> response : responses) {
            response.get();
        }

        for (Future<ClientResponse> r : responses2) {
            r.get();
        }

        Thread.sleep(5_000);

        assertThat(bot.receivedUpdates, is(sentUpdates));
        assertThat(bot2.receivedUpdates, is(sentUpdates2));
    }

    @Test
    public void shouldReceiveWebhookWithValidSecret() throws Exception {
        WebhookBotOptions options = new WebhookBotOptions(null);
        options.setSecret("expected-secret");
        TestBot secureBot = new TestBot(client, "securebot", options);
        container.register(secureBot);

        Update update = Randoms.randomUpdate();
        byte[] bytes = serializer.serialize(update);

        container.handleRequest("/securebot", "POST", "expected-secret", new ByteArrayInputStream(bytes));

        assertThat(secureBot.receivedUpdates.contains(update), is(true));
    }

    @Test
    public void shouldRejectWebhookWithInvalidSecret() throws Exception {
        WebhookBotOptions options = new WebhookBotOptions(null);
        options.setSecret("expected-secret");
        TestBot secureBot = new TestBot(client, "securebot", options);
        container.register(secureBot);

        Update update = Randoms.randomUpdate();
        byte[] bytes = serializer.serialize(update);

        try {
            container.handleRequest("/securebot", "POST", "wrong-secret", new ByteArrayInputStream(bytes));
            fail("WebhookException expected");
        } catch (WebhookException e) {
            assertThat(e.getErrorCode(), is(401));
        }

        assertThat(secureBot.receivedUpdates.contains(update), is(false));
    }

    private class TestBot extends WebhookBot {
        private final String key;
        Set<Update> receivedUpdates = ConcurrentHashMap.newKeySet();

        TestBot(MaxClient client, String key) {
            this(client, key, WebhookBotOptions.DEFAULT);
        }

        TestBot(MaxClient client, String key, WebhookBotOptions options) {
            super(client, options);
            this.key = key;
        }

        @Override
        public Object onUpdate(Update update) {
            receivedUpdates.add(update);
            return null;
        }

        @Override
        public String getKey() {
            return key;
        }
    }
}
