package uz.ems.maydon24.botauth.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.ems.maydon24.botauth.service.face.ButtonService;
import uz.ems.maydon24.botauth.service.face.CallbackService;
import uz.ems.maydon24.models.entity.User;
import uz.ems.maydon24.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CallbackServiceImpl implements CallbackService {
    private final TelegramBot telegramBot;
    private final ButtonService buttonService;
    private final UserServiceImpl botAuthUserService;
    private final UserRepository userRepository;

    @Transactional
    public void renewCode(User user) {
        Integer oneTimeCode = botAuthUserService.generateOneTimeCode();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(2);

        String loginUrl = "https://gourmet.uz/login?code=" + oneTimeCode;

        SendResponse response = (SendResponse) telegramBot.execute(new EditMessageText(
                        user.getTelegramId(),
                        user.getMessageId(),
                        "🔒 Kod: \n<pre>" + oneTimeCode + "</pre>"
                                + "\n\n\uD83D\uDD17 Bosing va Kiring: "
                                + "<a href=\"" + loginUrl + "\">gourmet.uz/login</a>"
                )
                        .parseMode(ParseMode.HTML)
                        .replyMarkup(buttonService.sendRenewCodeBtn())
        );
        userRepository.updateVerifyCodeAndExpiration(user.getTelegramId(), oneTimeCode, expirationTime);
        userRepository.updateMessageId(user.getTelegramId(), response.message().messageId());
    }
}
