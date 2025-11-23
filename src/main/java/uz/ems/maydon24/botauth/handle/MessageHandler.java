package uz.ems.maydon24.botauth.handle;

import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.ems.maydon24.botauth.service.face.MessageService;
import uz.ems.maydon24.botauth.service.face.UserService;
import uz.ems.maydon24.models.entity.User;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler implements Consumer<Message> {

    private final UserService userService;
    private final MessageService messageService;

    @Override
    public void accept(Message message) {
        String text = message.text();
        User user = userService.getOrCreateUser(message.from());

        if (message.contact() != null) {
            Contact contact = message.contact();
            messageService.removeKeyboardAndSendMsg(user.getTelegramId());
            messageService.sendCode(user, null);
            userService.updateUserPhoneById(user.getTelegramId(), contact.phoneNumber());

        } else if (text.equals("/start")) {
            if (user.getVerifyCodeExpiration() != null && user.getVerifyCodeExpiration().isAfter(LocalDateTime.now())) {
                messageService.sendCode(user, user.getVerifyCode());

            } else messageService.sendStartMsg(user.getTelegramId(), user.getFirstName() + " " + user.getLastName());

        } else log.warn("Unknown message {}", text);
    }
}
