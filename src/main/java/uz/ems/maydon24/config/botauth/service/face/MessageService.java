package uz.ems.maydon24.config.botauth.service.face;

import org.springframework.stereotype.Service;
import uz.ems.maydon24.models.entity.User;

@Service
public interface MessageService {

    void sendStartMsg(Long chatId, String fullName);

    void removeKeyboardAndSendMsg(Long telegramId);

    void sendCode(User user);
}
