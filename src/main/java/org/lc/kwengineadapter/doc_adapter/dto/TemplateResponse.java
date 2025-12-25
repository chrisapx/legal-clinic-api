package org.lc.kwengineadapter.doc_adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.doc_adapter.entity.DocumentTemplate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateResponse {
    private Long id;
    private String name;
    private String category;
    private String description;
    private String content;
    private DocumentTemplate.TemplateFormat format;
    private Boolean active;
    private String version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TemplateResponse fromEntity(DocumentTemplate template) {
        return new TemplateResponse(
                template.getId(),
                template.getName(),
                template.getCategory(),
                template.getDescription(),
                template.getContent(),
                template.getFormat(),
                template.getActive(),
                template.getVersion(),
                template.getCreatedAt(),
                template.getUpdatedAt()
        );
    }
}
