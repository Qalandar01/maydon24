package uz.ems.maydon24.botauth.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.ems.maydon24.botauth.service.face.ButtonService;
import uz.ems.maydon24.botauth.service.face.MessageService;
import uz.ems.maydon24.models.entity.User;
import uz.ems.maydon24.repository.UserRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final TelegramBot telegramBot;
    private final ButtonService buttonService;
    private final UserServiceImpl botAuthUserService;
    private final UserRepository userRepository;

    @Override
    public void sendStartMsg(Long chatId, String fullName) {
        SendResponse execute = telegramBot.execute(new SendMessage(chatId,
                """
                        🇺🇿
                        Salom """ + fullName + "👋\n" +
                        """ 
                                @futbolchi'ning rasmiy botiga xush kelibsiz
                                
                                ⬇ Kontaktingizni yuboring (tugmani bosib)
                                """
        )
                .parseMode(ParseMode.HTML)
                        .replyMarkup(buttonService.sendShareContactBtn())
        );
    }

    @Override
    public void removeKeyboardAndSendMsg(Long chatId) {
        telegramBot.execute(new SendMessage(chatId, "🔑Muvaffaqiyatli ro'yhatdan o'tkazildi!")
                .parseMode(ParseMode.Markdown)
                .replyMarkup(new ReplyKeyboardRemove())
        );
    }

    @Transactional
    public void sendCode(User user, Integer oneTimeCode) {
        String loginUrl = "https://gourmet.uz/login?code=" + oneTimeCode;
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(2);

        if (oneTimeCode == null) {
            oneTimeCode = botAuthUserService.generateOneTimeCode();
        }

        if (oneTimeCode != null) {
            try {
                telegramBot.execute(new DeleteMessage(user.getTelegramId(), user.getMessageId()));
            } catch (NullPointerException e) {
                log.warn("Message id null on delete, user {}", user.getTelegramId());
            }
        }

        SendResponse response = telegramBot.execute(new SendMessage(user.getTelegramId(),
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
