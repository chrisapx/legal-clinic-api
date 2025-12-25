package org.lc.kwengineadapter.identity_service.repository;

import org.lc.kwengineadapter.identity_service.entity.PasswordResetToken;
import org.lc.kwengineadapter.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    List<PasswordResetToken> findByUser(User user);

    void deleteByExpiryDateBefore(LocalDateTime date);

    void deleteByUser(User user);
}
