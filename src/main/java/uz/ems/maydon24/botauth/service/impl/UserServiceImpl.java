package uz.ems.maydon24.botauth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.ems.maydon24.botauth.service.face.UserService;
import uz.ems.maydon24.models.entity.Role;
import uz.ems.maydon24.models.entity.User;
import uz.ems.maydon24.models.enums.RoleName;
import uz.ems.maydon24.repository.RoleRepository;
import uz.ems.maydon24.repository.UserRepository;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;

@Service("botAuthUserService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public User getOrCreateUser(com.pengrad.telegrambot.model.User telegramUser) {
        return userRepository.findByTelegramId(telegramUser.id())
                .orElseGet(() -> createNewUser(telegramUser));
    }

    private User createNewUser(com.pengrad.telegrambot.model.User telegramUser) {
        // Default role
        Role roleUser = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role ROLE_USER not found!"));

        String fullName = telegramUser.lastName() != null
                ? telegramUser.firstName() + " " + telegramUser.lastName()
                : telegramUser.firstName();

        User user = User.builder()
                .telegramId(telegramUser.id())
                .telegramUsername(telegramUser.username())
                .phoneNumber(telegramUser.id().toString())
                .fullName(fullName)
                .visibility(true)
                .roles(Collections.singleton(roleUser))
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateOneTimeCode(Long userId) {
        Integer code = generateOneTimeCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(2);
        userRepository.updateVerifyCodeAndExpiration(userId, code, expiresAt);
    }

    @Override
    @Transactional
    public void updateTgUser(Long userId, User updatedUser) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setFullName(updatedUser.getFullName());
            user.setTelegramUsername(updatedUser.getTelegramUsername());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            userRepository.save(user);
        });
    }

    @Override
    @Transactional
    public void updateUserPhoneById(Long userId, String phoneNumber) {
        userRepository.updatePhoneByUserId(userId, phoneNumber);
    }

    @Override
    public Integer generateOneTimeCode() {
        return 100000 + new SecureRandom().nextInt(900000);
    }
}
