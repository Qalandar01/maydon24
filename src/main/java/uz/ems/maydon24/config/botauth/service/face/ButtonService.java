package uz.ems.maydon24.config.botauth.service.face;

import com.pengrad.telegrambot.model.request.Keyboard;
import org.springframework.stereotype.Service;

@Service
public interface ButtonService {
    Keyboard sendShareContactBtn();
}
