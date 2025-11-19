package uz.ems.maydon24.botauth.service.impl;

import com.pengrad.telegrambot.model.request.*;
import org.springframework.stereotype.Service;
import uz.ems.maydon24.botauth.service.face.ButtonService;

@Service
public class ButtonServiceImpl implements ButtonService {

    @Override
    public Keyboard sendShareContactBtn() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton("Kontaktni ulashish").requestContact(true)
        ).resizeKeyboard(true);
    }

    @Override
    public InlineKeyboardMarkup sendRenewCodeBtn() {
        return new InlineKeyboardMarkup(new InlineKeyboardButton("🔄Yangilash").callbackData("renew_code"));
    }
}
