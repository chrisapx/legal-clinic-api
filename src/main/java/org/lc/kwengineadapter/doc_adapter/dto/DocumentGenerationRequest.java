package org.lc.kwengineadapter.doc_adapter.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.doc_adapter.entity.GeneratedDocument;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentGenerationRequest {

    @NotNull(message = "Template ID is required")
    private Long templateId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Format is required")
    private GeneratedDocument.DocumentFormat format;

    private Map<String, Object> data;
}
