package org.lc.kwengineadapter.doc_adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.doc_adapter.dto.TemplateRequest;
import org.lc.kwengineadapter.doc_adapter.dto.TemplateResponse;
import org.lc.kwengineadapter.doc_adapter.service.DocumentTemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-templates")
@RequiredArgsConstructor
public class DocumentTemplateController {

    private final DocumentTemplateService templateService;

    @PostMapping
    public ResponseEntity<ApiResponse<TemplateResponse>> createTemplate(@Valid @RequestBody TemplateRequest request) {
        TemplateResponse template = templateService.createTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Template created successfully", template));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TemplateResponse>> getTemplateById(@PathVariable Long id) {
        TemplateResponse template = templateService.getTemplateById(id);
        return ResponseEntity.ok(ApiResponse.success(template));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TemplateResponse>>> getAllTemplates() {
        List<TemplateResponse> templates = templateService.getAllTemplates();
        return ResponseEntity.ok(ApiResponse.success(templates));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<TemplateResponse>>> getTemplatesByCategory(@PathVariable String category) {
        List<TemplateResponse> templates = templateService.getTemplatesByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(templates));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<TemplateResponse>>> getActiveTemplates() {
        List<TemplateResponse> templates = templateService.getActiveTemplates();
        return ResponseEntity.ok(ApiResponse.success(templates));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TemplateResponse>> updateTemplate(
            @PathVariable Long id,
            @Valid @RequestBody TemplateRequest request) {
        TemplateResponse template = templateService.updateTemplate(id, request);
        return ResponseEntity.ok(ApiResponse.success("Template updated successfully", template));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<TemplateResponse>> deactivateTemplate(@PathVariable Long id) {
        TemplateResponse template = templateService.deactivateTemplate(id);
        return ResponseEntity.ok(ApiResponse.success("Template deactivated successfully", template));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.ok(ApiResponse.success("Template deleted successfully", null));
    }
}
