package org.lc.kwengineadapter.common.controller;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.common.service.EmailVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email-verification")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        emailVerificationService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.success("Email verified successfully! You can now log in.", null));
    }

    @PostMapping("/resend")
    public ResponseEntity<ApiResponse<Void>> resendVerificationEmail(@RequestParam String email) {
        emailVerificationService.resendVerificationEmail(email);
        return ResponseEntity.ok(ApiResponse.success("Verification email sent! Please check your inbox.", null));
    }
}
