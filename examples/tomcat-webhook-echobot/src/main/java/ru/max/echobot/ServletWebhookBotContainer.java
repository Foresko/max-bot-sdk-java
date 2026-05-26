package ru.max.echobot;

import java.io.IOException;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import ru.max.bot.exceptions.WebhookException;
import ru.max.bot.webhook.WebhookBot;
import ru.max.bot.webhook.WebhookBotContainer;
import ru.max.bot.webhook.WebhookBotContainerBase;

public class ServletWebhookBotContainer extends WebhookBotContainerBase implements Servlet {
    private final String serverUrl;
    private final HttpServlet servlet;

    ServletWebhookBotContainer(String serverUrl) {
        this.serverUrl = serverUrl;
        this.servlet = new DelegatingBotServlet();
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        servlet.init(servletConfig);
    }

    @Override
    public ServletConfig getServletConfig() {
        return servlet.getServletConfig();
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException,
            IOException {
        servlet.service(servletRequest, servletResponse);
    }

    @Override
    public String getServletInfo() {
        return servlet.getServletInfo();
    }

    @Override
    public void destroy() {
        servlet.destroy();
    }

    @Override
    public String getWebhookUrl(WebhookBot bot) {
        return String.format("https://%s/%s/%s", serverUrl, "bots", bot.getKey());
    }

    private class DelegatingBotServlet extends HttpServlet {
        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String webhookResponse;
            try {
                webhookResponse = handleRequest(
                        req.getPathInfo(),
                        req.getMethod(),
                        req.getHeader(WebhookBotContainer.SECRET_HEADER),
                        req.getInputStream()
                );
            } catch (WebhookException e) {
                resp.sendError(e.getErrorCode(), e.getMessage());
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            if (webhookResponse == null) {
                return;
            }

            resp.getWriter().println(webhookResponse);
        }
    }
}
