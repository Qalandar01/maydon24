package uz.ems.maydon24.config.botauth.service.face;

import org.springframework.stereotype.Service;
import uz.ems.maydon24.models.entity.User;

@Service
public interface UserService {

    User getOrCreateUser(com.pengrad.telegrambot.model.User tgUser);

    void updateTgUser(Long tgUserId, User user);

    Integer generateOneTimeCode();

    void updateOneTimeCode(Long userId);

    void updateUserPhoneById(Long userId, String phoneNumber);
}
