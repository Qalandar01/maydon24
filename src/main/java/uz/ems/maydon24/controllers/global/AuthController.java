package uz.ems.maydon24.controllers.global;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.ems.maydon24.models.dto.response.LoginRes;
import uz.ems.maydon24.service.face.AuthService;

import static uz.ems.maydon24.utils.Constants.*;

@RestController
@RequestMapping((API + V1 + AUTH))
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/code/{code}")
    public ResponseEntity<LoginRes> verifyUserWithCode(@PathVariable Integer code, HttpServletResponse response) {
        System.out.println(code);
        LoginRes loginRes = authService.verifyWithCodeAndSendUserData(code, response);
        return new ResponseEntity<>(loginRes, HttpStatus.ACCEPTED);
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginRes> logout(HttpServletResponse response) {
        authService.logout(response);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
