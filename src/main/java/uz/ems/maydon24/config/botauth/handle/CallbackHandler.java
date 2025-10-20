package uz.ems.maydon24.config.botauth.handle;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.ems.maydon24.config.botauth.service.face.MessageService;
import uz.ems.maydon24.config.botauth.service.face.UserService;
import uz.ems.maydon24.models.entity.User;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallbackHandler implements Consumer<CallbackQuery> {

    private final UserService userService;
    private final MessageService messageService;
    private final TelegramBot telegramBot;

    @Override
    public void accept(CallbackQuery callbackQuery) {
        String data = callbackQuery.data();
        String callbackId = callbackQuery.id();
        User user = userService.getOrCreateUser(callbackQuery.from());

        if (data.equals("renew_code")) {
            if (user.getVerifyCodeExpiration().isAfter(LocalDateTime.now())) {
                telegramBot.execute(new AnswerCallbackQuery(callbackId)
                        .text("Eski kodingiz hali ham kuchda ☝️")
                        .showAlert(true));
                return;
            }

            messageService.renewCode(user);
        }
    }
}
