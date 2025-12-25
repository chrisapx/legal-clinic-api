package org.lc.kwengineadapter.learn_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.learn_service.dto.ChatMessageResponse;
import org.lc.kwengineadapter.learn_service.entity.ChatMessage;
import org.lc.kwengineadapter.learn_service.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageResponse getChatMessageById(Long id) {
        ChatMessage message = chatMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChatMessage", "id", id));
        return ChatMessageResponse.fromEntity(message);
    }

    public List<ChatMessageResponse> getChatHistoryByUser(Long userId) {
        return chatMessageRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(ChatMessageResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ChatMessageResponse> getAllChatMessages() {
        return chatMessageRepository.findAll().stream()
                .map(ChatMessageResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ChatMessageResponse> getFailedMessages() {
        return chatMessageRepository.findByStatus(ChatMessage.MessageStatus.FAILED).stream()
                .map(ChatMessageResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
