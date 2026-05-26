package ru.max.echobot;

import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.max.bot.webhook.WebhookBot;
import ru.max.bot.webhook.WebhookBotOptions;
import ru.max.botapi.model.Update;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class WebhookEchoBot extends WebhookBot {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Options OPTIONS = new Options();

    private final EchoHandler handler;

    private WebhookEchoBot(String accessToken) {
        super(accessToken, WebhookBotOptions.DEFAULT);
        this.handler = new EchoHandler(getClient());
    }

    public static void main(String[] args) throws Exception {
        OptionSet optionSet;
        try {
            optionSet = OPTIONS.parse(args);
        } catch (OptionException e) {
            System.err.println(e.getMessage());
            System.exit(1);
            return;
        }

        String accessToken = OPTIONS.accessToken.value(optionSet);
        WebhookEchoBot bot = new WebhookEchoBot(accessToken);

        int port = OPTIONS.port.value(optionSet);
        Tomcat tomcat = new Tomcat();
        tomcat.getService().addConnector(getSslConnector(port));

        Path base = Files.createTempDirectory("echobot");
        Context rootCtx = tomcat.addContext("", base.toAbsolutePath().toString());


        String serverUrl = OPTIONS.host.value(optionSet);
        if (OPTIONS.port.value(optionSet) != null) {
            serverUrl += ":" + OPTIONS.port.value(optionSet);
        }

        ServletWebhookBotContainer botContainer = new ServletWebhookBotContainer(serverUrl);
        botContainer.register(bot);

        String servletName = "Bots";
        Tomcat.addServlet(rootCtx, servletName, botContainer);
        rootCtx.addServletMappingDecoded("/bots/*", servletName);

        tomcat.start();
        botContainer.start();

        tomcat.getServer().await();
    }

    @Override
    public Object onUpdate(Update update) {
        LOG.info("Handling update: {}", update);
        update.visit(handler);
        return null;
    }

    private static Connector getSslConnector(int port) throws URISyntaxException {
        Connector connector = new Connector("HTTP/1.1");
        connector.setPort(port);
        connector.setSecure(true);
        connector.setScheme("https");

        Path crtFile = getPath("localhost.crt");
        Path keyFile = getPath("localhost.key");

        connector.setProperty("SSLEnabled", "true");

        SSLHostConfig sslHostConfig = new SSLHostConfig();
        sslHostConfig.setProtocols("TLSv1.2,TLSv1.3");
        sslHostConfig.setHonorCipherOrder(true);
        sslHostConfig.setDisableCompression(true);
        sslHostConfig.setCertificateVerification("optional");

        SSLHostConfigCertificate certificate = new SSLHostConfigCertificate(
                sslHostConfig,
                SSLHostConfigCertificate.Type.RSA
        );
        certificate.setCertificateFile(crtFile.toString());
        certificate.setCertificateKeyFile(keyFile.toString());
        sslHostConfig.addCertificate(certificate);
        connector.addSslHostConfig(sslHostConfig);
        return connector;
    }

    private static Path getPath(String name) throws URISyntaxException {
        URL resource = Objects.requireNonNull(WebhookEchoBot.class.getClassLoader().getResource(name), name);
        return Paths.get(resource.toURI()).toAbsolutePath();
    }

    private static class Options extends OptionParser {
        OptionSpec<String> accessToken = accepts("token")
                .withRequiredArg()
                .required()
                .ofType(String.class);

        OptionSpec<String> host = accepts("host")
                .withRequiredArg()
                .defaultsTo("0.0.0.0")
                .ofType(String.class);

        OptionSpec<Integer> port = accepts("port")
                .withRequiredArg()
                .ofType(Integer.class);
    }
}
