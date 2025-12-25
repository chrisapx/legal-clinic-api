package org.lc.kwengineadapter.identity_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.BadRequestException;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.identity_service.dto.UserRequest;
import org.lc.kwengineadapter.identity_service.dto.UserResponse;
import org.lc.kwengineadapter.identity_service.entity.Role;
import org.lc.kwengineadapter.identity_service.entity.User;
import org.lc.kwengineadapter.identity_service.repository.RoleRepository;
import org.lc.kwengineadapter.identity_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", request.getRoleId()));

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        // Generate temporary password if password not provided
        String password = request.getPassword() != null && !request.getPassword().trim().isEmpty()
            ? request.getPassword()
            : UUID.randomUUID().toString().substring(0, 16);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(role);
        user.setActive(true);
        user.setEmailVerified(true); // Admin-created users are pre-verified

        User savedUser = userRepository.save(user);

        // TODO: Send password setup email if password was generated

        return UserResponse.fromEntity(savedUser);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return UserResponse.fromEntity(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return UserResponse.fromEntity(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", request.getRoleId()));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(role);

        User updatedUser = userRepository.save(user);
        return UserResponse.fromEntity(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }
}
