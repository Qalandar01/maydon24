package uz.ems.maydon24.service.face;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import uz.ems.maydon24.models.dto.response.LoginRes;

@Service
public interface AuthService {
    LoginRes verifyWithCodeAndSendUserData(Integer code, HttpServletResponse response);

    void logout(HttpServletResponse response);
}

