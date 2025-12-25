package org.lc.kwengineadapter.learn_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationListResponse {
    private Long id;
    private Long userId;
    private String title;
    private Boolean archived;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer messageCount;
    private String lastMessage;
}
