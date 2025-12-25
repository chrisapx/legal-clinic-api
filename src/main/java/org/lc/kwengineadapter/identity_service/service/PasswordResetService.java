package org.lc.kwengineadapter.identity_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.common.service.EmailService;
import org.lc.kwengineadapter.identity_service.entity.PasswordResetToken;
import org.lc.kwengineadapter.identity_service.entity.User;
import org.lc.kwengineadapter.identity_service.repository.PasswordResetTokenRepository;
import org.lc.kwengineadapter.identity_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void initiatePasswordReset(String email, String ipAddress) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Delete any existing tokens for this user
        passwordResetTokenRepository.deleteByUser(user);

        // Create new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        resetToken.setUsed(false);
        resetToken.setIpAddress(ipAddress);

        passwordResetTokenRepository.save(resetToken);

        // Send email
        emailService.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), token);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));

        if (resetToken.getUsed()) {
            throw new IllegalStateException("This reset link has already been used");
        }

        if (resetToken.isExpired()) {
            throw new IllegalStateException("This reset link has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        // Send confirmation email
        emailService.sendPasswordChangedEmail(user.getEmail(), user.getFirstName());
    }

    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .map(t -> !t.getUsed() && !t.isExpired())
                .orElse(false);
    }
}
