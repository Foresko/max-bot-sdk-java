package ru.max.bot;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;

import ru.max.bot.annotations.CommandHandler;
import ru.max.bot.annotations.UpdateHandler;
import ru.max.bot.builders.NewMessageBodyBuilder;
import ru.max.bot.commands.CommandLine;
import ru.max.botapi.client.MaxClient;
import ru.max.botapi.model.BotStartedUpdate;
import ru.max.botapi.model.Message;
import ru.max.botapi.model.MessageBody;
import ru.max.botapi.model.MessageCreatedUpdate;
import ru.max.botapi.model.MessageEditedUpdate;
import ru.max.botapi.model.MessageRemovedUpdate;
import ru.max.botapi.model.NewMessageBody;
import ru.max.botapi.model.Update;
import ru.max.botapi.model.User;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MaxBotBaseTest {

    private NewMessageBody mockResponse = NewMessageBodyBuilder.ofText("test").build();
    private MaxClient client = mock(MaxClient.class);
    private TestBot testBot;

    @Before
    public void setUp() {
        testBot = new TestBot(client);
    }

    @Test
    public void shouldHandleUpdate() {
        Message message = mock(Message.class);
        MessageBody body = mock(MessageBody.class);
        when(body.getText()).thenReturn("text");
        when(message.getBody()).thenReturn(body);
        Update update = new MessageCreatedUpdate(message, 1L);
        Object response = testBot.onUpdate(update);
        testBot.verify();
        assertThat(response, is(nullValue()));
    }

    @Test
    public void shouldHandleUpdateReturnNoResponse() {
        Update update = new BotStartedUpdate(1L, mock(User.class), 1L);
        Object response = testBot.onUpdate(update);
        testBot.verify();
        assertThat(response, is(nullValue()));
    }

    @Test
    public void shouldReturnAnyResponse() {
        Update update = new MessageRemovedUpdate("asd", 1L, 2L, 3L);
        Object response = testBot.onUpdate(update);
        testBot.verify();
        assertThat(response, is(mockResponse));
    }

    @Test
    public void shouldNotHandleUpdate() {
        Update update = new MessageEditedUpdate(mock(Message.class), 1L);
        Object response = testBot.onUpdate(update);
        assertThat(response, is(nullValue()));
    }

    @Test
    public void shouldInvokePrivateMethod() {
        BotWithPrivateMethod bot = new BotWithPrivateMethod(client);
        Update update = new MessageCreatedUpdate(mock(Message.class), 1L);
        Object response = bot.onUpdate(update);
        assertThat(response, is(mockResponse));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotInvokeMethodWithManyArgs() {
        new InvalidBot2(client);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotInvokeMethodWithNonUpdateArg() {
        new InvalidBot3(client);
    }

    @Test
    public void shouldHandleCommand() {
        Message message = mock(Message.class);
        MessageBody body = mock(MessageBody.class);
        when(body.getText()).thenReturn("/command1 arg");
        when(message.getBody()).thenReturn(body);
        Update update = new MessageCreatedUpdate(message, 1L);
        Object response = testBot.onUpdate(update);
        testBot.verify();
        assertThat(response, is("arg"));
    }

    @Test
    public void shouldHandleCommand2() {
        Message message = mock(Message.class);
        MessageBody body = mock(MessageBody.class);
        when(body.getText()).thenReturn("/command2 arg arg2");
        when(message.getBody()).thenReturn(body);
        Update update = new MessageCreatedUpdate(message, 1L);
        Object response = testBot.onUpdate(update);
        testBot.verify();
        assertThat(response, is("arg"));
    }

    @Test
    public void shouldHandleAsUpdate() {
        Message message = mock(Message.class);
        MessageBody body = mock(MessageBody.class);
        when(body.getText()).thenReturn("/notcommand arg");
        when(message.getBody()).thenReturn(body);
        Update update = new MessageCreatedUpdate(message, 1L);
        CommandTestBot bot = new CommandTestBot(client);
        Object response = bot.onUpdate(update);
        assertThat(response, is(update));
    }

    @Test
    public void shouldHandleAsUpdate2() {
        Message message = mock(Message.class);
        MessageBody body = mock(MessageBody.class);
        when(body.getText()).thenReturn("");
        when(message.getBody()).thenReturn(body);
        Update update = new MessageCreatedUpdate(message, 1L);
        CommandTestBot bot = new CommandTestBot(client);
        Object response = bot.onUpdate(update);
        assertThat(response, is(update));
    }

    @Test
    public void shouldRegisterHandlers() {
        AtomicBoolean handled = new AtomicBoolean();
        AtomicBoolean handled2 = new AtomicBoolean();
        Object handler1 = new Object() {
            @UpdateHandler
            public void handle(MessageCreatedUpdate update) {
                handled.compareAndSet(false, true);
            }
        };

        Object handler2 = new Object() {
            @UpdateHandler
            public void handle(MessageEditedUpdate update) {
                handled2.compareAndSet(false, true);
            }
        };

        MaxBot bot = new MaxBotBase(client, handler1, handler2);
        bot.onUpdate(new MessageCreatedUpdate(mock(Message.class), System.currentTimeMillis()));
        bot.onUpdate(new MessageEditedUpdate(mock(Message.class), System.currentTimeMillis()));
        assertThat(handled.get(), is(true));
        assertThat(handled2.get(), is(true));
    }

    @Test
    public void shouldParseArgs() {
        ArgsTestBot bot = new ArgsTestBot(client);
        Message message = mock(Message.class);
        MessageBody body = mock(MessageBody.class);
        when(body.getText()).thenReturn("/command arg1 arg2");
        when(message.getBody()).thenReturn(body);
        Update update = new MessageCreatedUpdate(message, 1L);
        Object response = bot.onUpdate(update);
        assertThat(response, is(new String[]{"arg1", "arg2"}));
    }

    @Test
    public void shouldParseArgs2() {
        ArgsTestBot bot = new ArgsTestBot(client);
        Message message = mock(Message.class);
        MessageBody body = mock(MessageBody.class);
        when(body.getText()).thenReturn("/command arg1");
        when(message.getBody()).thenReturn(body);
        Update update = new MessageCreatedUpdate(message, 1L);
        Object response = bot.onUpdate(update);
        assertThat(response, is(new String[]{"arg1", null}));
    }

    @Test
    public void shouldParseArgs3() {
        ArgsTestBot bot = new ArgsTestBot(client);
        Message message = mock(Message.class);
        MessageBody body = mock(MessageBody.class);
        when(body.getText()).thenReturn("/command arg1 arg2 arg3");
        when(message.getBody()).thenReturn(body);
        Update update = new MessageCreatedUpdate(message, 1L);
        Object response = bot.onUpdate(update);
        assertThat(response, is(new String[]{"arg1", "arg2"}));
    }

    @Test
    public void shouldNotParseArgs() {
        ArgsTestBot bot = new ArgsTestBot(client);
        Message message = mock(Message.class);
        MessageBody body = mock(MessageBody.class);
        when(body.getText()).thenReturn("/commandWithoutArgs arg1 arg2 arg3");
        when(message.getBody()).thenReturn(body);
        Update update = new MessageCreatedUpdate(message, 1L);
        Object response = bot.onUpdate(update);
        assertThat(response, is("arg1 arg2 arg3"));
    }

    @Test
    public void shouldNotParseArgs2() {
        ArgsTestBot bot = new ArgsTestBot(client);
        Message message = mock(Message.class);
        MessageBody body = mock(MessageBody.class);
        when(body.getText()).thenReturn("/command2");
        when(message.getBody()).thenReturn(body);
        Update update = new MessageCreatedUpdate(message, 1L);
        Object response = bot.onUpdate(update);
        assertThat(response, is(message));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBotWithInvalidCommand() {
        new InvalidBot4(client);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBotWithInvalidCommand2() {
        new InvalidBot5(client);
    }

    private class TestBot extends MaxBotBase {
        private final AtomicBoolean handled;

        TestBot(MaxClient client) {
            super(client);
            this.handled = new AtomicBoolean();
        }

        @UpdateHandler
        public void onMessageCreated(MessageCreatedUpdate update) {
            assertThat(update, is(notNullValue()));
            signal();
        }

        @UpdateHandler
        public Object onBotStarted(BotStartedUpdate update) {
            assertThat(update, is(notNullValue()));
            signal();
            return null;
        }

        @UpdateHandler
        public NewMessageBody onMessageRemoved(MessageRemovedUpdate update) {
            assertThat(update, is(notNullValue()));
            signal();
            return mockResponse;
        }

        @CommandHandler("command1")
        public String onCommand1(Message message, String arg) {
            assertThat(arg, is(notNullValue()));
            assertThat(message, is(notNullValue()));
            signal();
            return arg;
        }

        @CommandHandler("command2")
        public String onCommand2(Message message, String arg) {
            assertThat(arg, is(notNullValue()));
            assertThat(message, is(notNullValue()));
            signal();
            return arg;
        }

        @CommandHandler("command3")
        public void onCommand3(Message message, CommandLine commandLine) {
            assertThat(commandLine, is(notNullValue()));
            assertThat(message, is(notNullValue()));
            signal();
        }

        private void signal() {
            if (!handled.compareAndSet(false, true)) {
                fail();
            }
        }

        private void verify() {
            assertThat(handled.get(), is(true));
        }
    }

    private class BotWithPrivateMethod extends MaxBotBase {
        BotWithPrivateMethod(MaxClient client) {
            super(client);
        }

        @UpdateHandler
        private NewMessageBody onMessageCreated(MessageCreatedUpdate update) {
            return mockResponse;
        }
    }

    private class InvalidBot2 extends MaxBotBase {
        InvalidBot2(MaxClient client) {
            super(client);
        }

        @UpdateHandler
        public NewMessageBody onMessageCreated(MessageCreatedUpdate update, Object arg2) {
            fail();
            return mockResponse;
        }
    }

    private class InvalidBot3 extends MaxBotBase {
        InvalidBot3(MaxClient client) {
            super(client);
        }

        @UpdateHandler
        public NewMessageBody onMessageCreated(Object arg) {
            fail();
            return mockResponse;
        }
    }

    private class InvalidBot4 extends MaxBotBase {
        InvalidBot4(MaxClient client) {
            super(client);
        }

        @CommandHandler("/")
        public NewMessageBody onMessageCreated(Object arg) {
            fail();
            return mockResponse;
        }
    }

    private class InvalidBot5 extends MaxBotBase {
        InvalidBot5(MaxClient client) {
            super(client);
        }

        @CommandHandler("")
        public NewMessageBody onMessageCreated(Object arg) {
            fail();
            return mockResponse;
        }
    }

    private class CommandTestBot extends MaxBotBase {
        CommandTestBot(MaxClient client) {
            super(client);
        }

        @UpdateHandler
        public MessageCreatedUpdate onMessageCreated(MessageCreatedUpdate update) {
            return update;
        }

        @CommandHandler("command")
        public void onCommand(Message message, String arg1, String arg2) {
            fail();
        }
    }

    private class ArgsTestBot extends MaxBotBase {
        ArgsTestBot(MaxClient client) {
            super(client);
        }

        @UpdateHandler
        public MessageCreatedUpdate onMessageCreated(MessageCreatedUpdate update) {
            return update;
        }

        @CommandHandler("command")
        public String[] onCommand(Message message, String arg1, String arg2) {
            return new String[]{arg1, arg2};
        }

        @CommandHandler("command2")
        public Message onCommand(Message message) {
            return message;
        }

        @CommandHandler(value = "commandWithoutArgs", parseArgs = false)
        public String onCommand(Message message, String tail) {
            return tail;
        }
    }

}
