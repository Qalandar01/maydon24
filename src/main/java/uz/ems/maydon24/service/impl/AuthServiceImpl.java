package uz.ems.maydon24.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.ems.maydon24.config.security.JwtService;
import uz.ems.maydon24.models.dto.response.LoginRes;
import uz.ems.maydon24.models.dto.response.UserRes;
import uz.ems.maydon24.models.entity.User;
import uz.ems.maydon24.models.enums.RoleName;
import uz.ems.maydon24.repository.UserRepository;
import uz.ems.maydon24.service.face.AuthService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public LoginRes verifyWithCodeAndSendUserData(Integer code, HttpServletResponse response) {
        User user = userRepository.findOneByVerifyCode(code)
                .orElseThrow(() -> new RuntimeException("User not found for code: " + code));

        if (user.getVerifyCodeExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Code expired");
        }
        String token = jwtService.generateToken(user);

        jwtService.setJwtCookie(response, token);

        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserRes userRes = UserRes.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .isAdmin(user.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_ADMIN)))
                .build();

        return LoginRes.builder()
                .token(token)
                .userRes(userRes)
                .build();
    }

    @Override
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("ll-token", null);
        cookie.setHttpOnly(true);
        cookie.setDomain("gourmet.uz");
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }
}
