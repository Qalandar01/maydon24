package uz.ems.maydon24.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.ems.maydon24.models.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByVerifyCode(Integer verifyCode);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByTelegramId(Long telegramId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.phoneNumber = :phoneNumber WHERE u.telegramId = :userId")
    void updatePhoneByUserId(@Param("userId") Long userId, @Param("phoneNumber") String phoneNumber);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.messageId = :messageId WHERE u.telegramId = :userId")
    void updateMessageId(@Param("userId") Long userId, @Param("messageId") Integer messageId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.verifyCode = :code, u.verifyCodeExpiration = :expiration WHERE u.telegramId = :userId")
    void updateVerifyCodeAndExpiration(@Param("userId") Long userId,
                                       @Param("code") Integer oneTimeCode,
                                       @Param("expiration") LocalDateTime expirationTime);
}
