package org.lc.kwengineadapter.learn_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.learn_service.dto.*;
import org.lc.kwengineadapter.learn_service.service.ConversationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ConversationResponse>> createConversation(
            @Valid @RequestBody CreateConversationRequest request) {
        ConversationResponse response = conversationService.createConversation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Conversation created successfully", response));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ConversationListResponse>>> getUserConversations(
            @PathVariable Long userId,
            @RequestParam(required = false) Boolean archived,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<ConversationListResponse> conversations = conversationService
                .getUserConversations(userId, archived, page, size);
        return ResponseEntity.ok(ApiResponse.success(conversations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ConversationResponse>> getConversation(
            @PathVariable Long id,
            @RequestParam Long userId) {
        ConversationResponse conversation = conversationService.getConversationById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(conversation));
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getConversationMessages(
            @PathVariable Long conversationId,
            @RequestParam Long userId) {
        List<ChatMessageResponse> messages = conversationService
                .getConversationMessages(conversationId, userId);
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ConversationResponse>> updateConversation(
            @PathVariable Long id,
            @RequestParam Long userId,
            @Valid @RequestBody UpdateConversationRequest request) {
        ConversationResponse conversation = conversationService
                .updateConversation(id, userId, request);
        return ResponseEntity.ok(ApiResponse.success("Conversation updated successfully", conversation));
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<ApiResponse<Void>> archiveConversation(
            @PathVariable Long id,
            @RequestParam Long userId) {
        conversationService.archiveConversation(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Conversation archived successfully", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteConversation(
            @PathVariable Long id,
            @RequestParam Long userId) {
        conversationService.deleteConversation(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Conversation deleted successfully", null));
    }
}
