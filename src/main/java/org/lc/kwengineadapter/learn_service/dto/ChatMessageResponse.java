package org.lc.kwengineadapter.learn_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.learn_service.entity.ChatMessage;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private Long id;
    private Long conversationId;
    private Long userId;
    private String userMessage;
    private String aiResponse;
    private String context;
    private ChatMessage.MessageStatus status;
    private String errorMessage;
    private String model;
    private LocalDateTime createdAt;

    public static ChatMessageResponse fromEntity(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getConversation() != null ? message.getConversation().getId() : null,
                message.getUserId(),
                message.getUserMessage(),
                message.getAiResponse(),
                message.getContext(),
                message.getStatus(),
                message.getErrorMessage(),
                message.getModel(),
                message.getCreatedAt()
        );
    }
}
