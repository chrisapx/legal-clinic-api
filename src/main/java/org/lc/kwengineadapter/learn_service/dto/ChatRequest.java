package org.lc.kwengineadapter.learn_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "User ID is required")
    private Long userId;

    private Long conversationId; // Optional: if null, creates new conversation

    private String context;
}
