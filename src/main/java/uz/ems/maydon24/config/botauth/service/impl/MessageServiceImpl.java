package uz.ems.maydon24.config.botauth.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.ems.maydon24.config.botauth.service.face.ButtonService;
import uz.ems.maydon24.config.botauth.service.face.MessageService;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final TelegramBot telegramBot;
    private final ButtonService buttonService;

    @Override
    public void sendStartMsg(Long chatId, String firstName) {
        SendResponse execute = telegramBot.execute(new SendMessage(chatId,
                """
                        🇺🇿
                        Salom """ + firstName + "👋\n" +
                        """ 
                                @futbolchi'ning rasmiy botiga xush kelibsiz
                                
                                ⬇ Kontaktingizni yuboring (tugmani bosib)
                                """
        )
                .parseMode(ParseMode.HTML)
                        .replyMarkup(buttonService.sendShareContactBtn())
        );
    }
}
