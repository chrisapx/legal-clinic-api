package org.lc.kwengineadapter.identity_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.identity_service.dto.AuthResponse;
import org.lc.kwengineadapter.identity_service.dto.LoginRequest;
import org.lc.kwengineadapter.identity_service.dto.RegisterRequest;
import org.lc.kwengineadapter.identity_service.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        AuthResponse response = authenticationService.login(request, httpRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {
        AuthResponse response = authenticationService.register(request, httpRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Registration successful", response));
    }
}
