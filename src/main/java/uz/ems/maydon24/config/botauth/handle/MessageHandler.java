package uz.ems.maydon24.config.botauth.handle;

import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uz.ems.maydon24.config.botauth.service.face.UserService;
import uz.ems.maydon24.models.entity.User;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler implements Consumer<Message> {

    private final UserService userService;

    @Override
    public void accept(Message message) {
        String text = message.text();
        User user = userService.getOrCreateUser(message.from());

    }
}
