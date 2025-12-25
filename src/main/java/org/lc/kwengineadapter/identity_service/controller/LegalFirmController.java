package org.lc.kwengineadapter.identity_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.identity_service.dto.LegalFirmRequest;
import org.lc.kwengineadapter.identity_service.dto.LegalFirmResponse;
import org.lc.kwengineadapter.identity_service.service.LegalFirmService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/legal-firms")
@RequiredArgsConstructor
public class LegalFirmController {

    private final LegalFirmService legalFirmService;

    @PostMapping
    public ResponseEntity<ApiResponse<LegalFirmResponse>> createLegalFirm(@Valid @RequestBody LegalFirmRequest request) {
        LegalFirmResponse firm = legalFirmService.createLegalFirm(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Legal firm created successfully", firm));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LegalFirmResponse>> getLegalFirmById(@PathVariable Long id) {
        LegalFirmResponse firm = legalFirmService.getLegalFirmById(id);
        return ResponseEntity.ok(ApiResponse.success(firm));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LegalFirmResponse>>> getAllLegalFirms() {
        List<LegalFirmResponse> firms = legalFirmService.getAllLegalFirms();
        return ResponseEntity.ok(ApiResponse.success(firms));
    }

    @GetMapping("/verified")
    public ResponseEntity<ApiResponse<List<LegalFirmResponse>>> getVerifiedLegalFirms() {
        List<LegalFirmResponse> firms = legalFirmService.getVerifiedLegalFirms();
        return ResponseEntity.ok(ApiResponse.success(firms));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LegalFirmResponse>> updateLegalFirm(
            @PathVariable Long id,
            @Valid @RequestBody LegalFirmRequest request) {
        LegalFirmResponse firm = legalFirmService.updateLegalFirm(id, request);
        return ResponseEntity.ok(ApiResponse.success("Legal firm updated successfully", firm));
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<ApiResponse<LegalFirmResponse>> verifyLegalFirm(@PathVariable Long id) {
        LegalFirmResponse firm = legalFirmService.verifyLegalFirm(id);
        return ResponseEntity.ok(ApiResponse.success("Legal firm verified successfully", firm));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLegalFirm(@PathVariable Long id) {
        legalFirmService.deleteLegalFirm(id);
        return ResponseEntity.ok(ApiResponse.success("Legal firm deleted successfully", null));
    }
}
