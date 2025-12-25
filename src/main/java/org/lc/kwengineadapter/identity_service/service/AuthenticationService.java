package org.lc.kwengineadapter.identity_service.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.common.security.JwtUtil;
import org.lc.kwengineadapter.common.service.EmailService;
import org.lc.kwengineadapter.common.service.EmailVerificationService;
import org.lc.kwengineadapter.identity_service.dto.AuthResponse;
import org.lc.kwengineadapter.identity_service.dto.LoginRequest;
import org.lc.kwengineadapter.identity_service.dto.RegisterRequest;
import org.lc.kwengineadapter.identity_service.entity.Permission;
import org.lc.kwengineadapter.identity_service.entity.Role;
import org.lc.kwengineadapter.identity_service.entity.User;
import org.lc.kwengineadapter.identity_service.entity.UserActivity;
import org.lc.kwengineadapter.identity_service.repository.RoleRepository;
import org.lc.kwengineadapter.identity_service.repository.UserActivityRepository;
import org.lc.kwengineadapter.identity_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserActivityRepository userActivityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;

    @Transactional
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        if (!user.getEmailVerified()) {
            throw new RuntimeException("Please verify your email address before logging in. Check your inbox for the verification link.");
        }

        if (!user.getActive()) {
            throw new RuntimeException("Account is inactive. Please contact administrator.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid email or password");
        }

        // Log activity
        logActivity(user, "LOGIN", "USER", user.getId(), "User logged in successfully", httpRequest);

        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole().getName());

        Set<String> permissions = user.getRole().getPermissions().stream()
                .map(Permission::getCode)
                .collect(Collectors.toSet());

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().getName(),
                permissions
        );
    }

    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Get default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default USER role not found. Please contact administrator."));

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(userRole);
        user.setActive(false); // Account inactive until email verified
        user.setEmailVerified(false);

        user = userRepository.save(user);

        // Log activity
        logActivity(user, "REGISTER", "USER", user.getId(), "New user registered - awaiting email verification", httpRequest);

        // Send verification email
        emailVerificationService.sendVerificationEmail(user);

        // Return null to indicate registration success but email verification required
        throw new RuntimeException("Registration successful! Please check your email to verify your account before logging in.");
    }

    public void logActivity(User user, String action, String entityType, Long entityId, String details, HttpServletRequest request) {
        UserActivity activity = new UserActivity();
        activity.setUser(user);
        activity.setAction(action);
        activity.setEntityType(entityType);
        activity.setEntityId(entityId);
        activity.setDetails(details);
        activity.setIpAddress(getClientIp(request));
        activity.setUserAgent(request.getHeader("User-Agent"));
        userActivityRepository.save(activity);
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
