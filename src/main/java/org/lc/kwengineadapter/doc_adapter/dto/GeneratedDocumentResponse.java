package org.lc.kwengineadapter.doc_adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.doc_adapter.entity.GeneratedDocument;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedDocumentResponse {
    private Long id;
    private Long templateId;
    private String templateName;
    private String fileName;
    private String filePath;
    private GeneratedDocument.DocumentFormat format;
    private Long userId;
    private String metadata;
    private GeneratedDocument.DocumentStatus status;
    private Long fileSize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static GeneratedDocumentResponse fromEntity(GeneratedDocument document) {
        return new GeneratedDocumentResponse(
                document.getId(),
                document.getTemplate().getId(),
                document.getTemplate().getName(),
                document.getFileName(),
                document.getFilePath(),
                document.getFormat(),
                document.getUserId(),
                document.getMetadata(),
                document.getStatus(),
                document.getFileSize(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }
}
