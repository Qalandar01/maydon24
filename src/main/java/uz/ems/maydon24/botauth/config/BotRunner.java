package uz.ems.maydon24.botauth.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.DeleteWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import uz.ems.maydon24.botauth.handle.CallbackHandler;
import uz.ems.maydon24.botauth.handle.MessageHandler;

import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotRunner implements CommandLineRunner {

    private final TelegramBot bot;
    private final ExecutorService executorService;
    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;

    @Value("${telegram.bot.enabled:true}")
    private boolean botEnabled;

    @Value("${telegram.bot.fail-fast:false}")
    private boolean botFailFast;

    @PostConstruct
    public void deleteWebhookIfExists() {
        if (!botEnabled) {
            log.info("Telegram bot disabled by configuration.");
            return;
        }

        try {
            BaseResponse response = bot.execute(new DeleteWebhook());
            if (response != null && response.isOk()) {
                log.info("Telegram webhook deleted successfully.");
            } else {
                log.warn("Failed to delete telegram webhook: {}",
                        response == null ? "null response" : response.description());
            }
        } catch (Exception e) {
            if (botFailFast) {
                throw new IllegalStateException("Telegram bot webhook init failed", e);
            }
            log.warn("Skipping telegram webhook init: {}", e.getMessage());
        }
    }

    @Override
    public void run(String... args) {
        if (!botEnabled) {
            return;
        }
        try {
            bot.setUpdatesListener(updates -> {
                for (Update update : updates) {
                    executorService.execute(() -> {
                        if (update.message() != null) {
                            messageHandler.accept(update.message());
                        } else if (update.callbackQuery() != null) {
                            callbackHandler.accept(update.callbackQuery());
                        } else {
                            log.warn("Unknown telegram update: {}", update);
                        }
                    });
                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            });
        } catch (Exception e) {
            if (botFailFast) {
                throw new IllegalStateException("Telegram bot listener init failed", e);
            }
            log.warn("Skipping telegram listener startup: {}", e.getMessage());
        }
    }
}
