package org.lc.kwengineadapter.learn_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConversationRequest {
    @NotBlank(message = "Title is required")
    private String title;
}
