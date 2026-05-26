package ru.max.bot.chat;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import ru.max.bot.Randoms;
import ru.max.bot.commands.Command;
import ru.max.bot.commands.CommandHandler;
import ru.max.bot.commands.CommandLine;
import ru.max.bot.Mocks;
import ru.max.botapi.model.Message;

import static ru.max.bot.Mocks.message;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ChatBotBuilderTest {
    @Test
    public void test1() {
        AtomicBoolean start = new AtomicBoolean();
        AtomicBoolean help = new AtomicBoolean();
        AtomicBoolean unknown = new AtomicBoolean();
        AtomicBoolean byDefault = new AtomicBoolean();
        AtomicBoolean handler = new AtomicBoolean();
        String randomKey = Randoms.text();
        ChatBotBuilder builder = new ChatBotBuilder()
                .on("start", (message, commandLine) -> {
                    if (!start.compareAndSet(false, true)) {
                        fail();
                    }
                })
                .on("help", (message, commandLine) -> {
                    if (!help.compareAndSet(false, true)) {
                        fail();
                    }
                })
                .add(new TestCommand() {
                    @Override
                    public String getKey() {
                        return randomKey;
                    }

                    @Override
                    public void execute(Message message, CommandLine commandLine) {
                        if (!handler.compareAndSet(false, true)) {
                            fail();
                        }
                    }
                })
                .onUnknownCommand((message, commandLine) -> {
                    if (!unknown.compareAndSet(false, true)) {
                        fail();
                    }
                })
                .byDefault(message -> {
                    if (!byDefault.compareAndSet(false, true)) {
                        fail();
                    }
                });
        ;

        ChatBot chatBot = builder.build();
        chatBot.replyOn(Mocks.message("/start"));
        chatBot.replyOn(Mocks.message("/help"));
        chatBot.replyOn(Mocks.message("/unknown"));
        chatBot.replyOn(Mocks.message("notcommand"));
        chatBot.replyOn(Mocks.message("/" + randomKey));

        assertThat(start.get(), is(true));
        assertThat(help.get(), is(true));
        assertThat(unknown.get(), is(true));
        assertThat(byDefault.get(), is(true));
        assertThat(handler.get(), is(true));
    }

    @Test
    public void test2() {
        AtomicBoolean start = new AtomicBoolean();
        ChatBotBuilder builder = new ChatBotBuilder() {{
            on("start", (message, commandLine) -> {
                if (!start.compareAndSet(false, true)) {
                    fail();
                }
            });
        }};

        ChatBot chatBot = builder.build();
        chatBot.replyOn(Mocks.message("/start"));
        chatBot.replyOn(Mocks.message("/unknown"));
        chatBot.replyOn(Mocks.message("notcommand"));
        assertThat(start.get(), is(true));
    }

    private abstract class TestCommand implements Command, CommandHandler {
    }
}