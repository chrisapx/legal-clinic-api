package org.lc.kwengineadapter.doc_adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.doc_adapter.dto.DocumentGenerationRequest;
import org.lc.kwengineadapter.doc_adapter.dto.GeneratedDocumentResponse;
import org.lc.kwengineadapter.doc_adapter.service.DocumentGenerationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentGenerationController {

    private final DocumentGenerationService documentService;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<GeneratedDocumentResponse>> generateDocument(
            @Valid @RequestBody DocumentGenerationRequest request) {
        GeneratedDocumentResponse document = documentService.generateDocument(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Document generation initiated", document));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GeneratedDocumentResponse>> getDocumentById(@PathVariable Long id) {
        GeneratedDocumentResponse document = documentService.getDocumentById(id);
        return ResponseEntity.ok(ApiResponse.success(document));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<GeneratedDocumentResponse>>> getDocumentsByUser(@PathVariable Long userId) {
        List<GeneratedDocumentResponse> documents = documentService.getDocumentsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GeneratedDocumentResponse>>> getAllDocuments() {
        List<GeneratedDocumentResponse> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok(ApiResponse.success("Document deleted successfully", null));
    }
}
