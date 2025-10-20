package uz.ems.maydon24.config.botauth.service.face;

import org.springframework.stereotype.Service;

@Service
public interface MessageService {

    void sendStartMsg(Long chatId, String firstName);

}
