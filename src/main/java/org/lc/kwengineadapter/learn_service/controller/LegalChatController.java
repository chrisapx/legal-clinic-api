package org.lc.kwengineadapter.learn_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.learn_service.dto.ChatMessageResponse;
import org.lc.kwengineadapter.learn_service.dto.ChatRequest;
import org.lc.kwengineadapter.learn_service.dto.ChatResponse;
import org.lc.kwengineadapter.learn_service.service.ChatMessageService;
import org.lc.kwengineadapter.learn_service.service.LegalChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class LegalChatController {

    private final LegalChatService chatService;
    private final ChatMessageService chatMessageService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatMessageResponse>> chat(@Valid @RequestBody ChatRequest request) {
        ChatMessageResponse response = chatService.chat(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChatHistory(@PathVariable Long userId) {
        List<ChatMessageResponse> history = chatMessageService.getChatHistoryByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(history));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<ApiResponse<ChatMessageResponse>> getChatMessage(@PathVariable Long id) {
        ChatMessageResponse message = chatMessageService.getChatMessageById(id);
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    @GetMapping("/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getAllChatMessages() {
        List<ChatMessageResponse> messages = chatMessageService.getAllChatMessages();
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    @GetMapping("/messages/failed")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getFailedMessages() {
        List<ChatMessageResponse> messages = chatMessageService.getFailedMessages();
        return ResponseEntity.ok(ApiResponse.success(messages));
    }
}

