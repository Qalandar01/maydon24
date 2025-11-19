package uz.ems.maydon24.botauth.service.face;

import org.springframework.stereotype.Service;
import uz.ems.maydon24.models.entity.User;

@Service
public interface CallbackService {
    void renewCode(User user);
}
