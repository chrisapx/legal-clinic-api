package org.lc.kwengineadapter.identity_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.identity_service.dto.LawyerRequest;
import org.lc.kwengineadapter.identity_service.dto.LawyerResponse;
import org.lc.kwengineadapter.identity_service.service.LawyerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lawyers")
@RequiredArgsConstructor
public class LawyerController {

    private final LawyerService lawyerService;

    @PostMapping
    public ResponseEntity<ApiResponse<LawyerResponse>> createLawyer(@Valid @RequestBody LawyerRequest request) {
        LawyerResponse lawyer = lawyerService.createLawyer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lawyer created successfully", lawyer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LawyerResponse>> getLawyerById(@PathVariable Long id) {
        LawyerResponse lawyer = lawyerService.getLawyerById(id);
        return ResponseEntity.ok(ApiResponse.success(lawyer));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LawyerResponse>>> getAllLawyers() {
        List<LawyerResponse> lawyers = lawyerService.getAllLawyers();
        return ResponseEntity.ok(ApiResponse.success(lawyers));
    }

    @GetMapping("/firm/{firmId}")
    public ResponseEntity<ApiResponse<List<LawyerResponse>>> getLawyersByFirm(@PathVariable Long firmId) {
        List<LawyerResponse> lawyers = lawyerService.getLawyersByFirm(firmId);
        return ResponseEntity.ok(ApiResponse.success(lawyers));
    }

    @GetMapping("/verified")
    public ResponseEntity<ApiResponse<List<LawyerResponse>>> getVerifiedLawyers() {
        List<LawyerResponse> lawyers = lawyerService.getVerifiedLawyers();
        return ResponseEntity.ok(ApiResponse.success(lawyers));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LawyerResponse>> updateLawyer(
            @PathVariable Long id,
            @Valid @RequestBody LawyerRequest request) {
        LawyerResponse lawyer = lawyerService.updateLawyer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Lawyer updated successfully", lawyer));
    }

    @PatchMapping("/{id}/verify")
    public ResponseEntity<ApiResponse<LawyerResponse>> verifyLawyer(@PathVariable Long id) {
        LawyerResponse lawyer = lawyerService.verifyLawyer(id);
        return ResponseEntity.ok(ApiResponse.success("Lawyer verified successfully", lawyer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLawyer(@PathVariable Long id) {
        lawyerService.deleteLawyer(id);
        return ResponseEntity.ok(ApiResponse.success("Lawyer deleted successfully", null));
    }
}
