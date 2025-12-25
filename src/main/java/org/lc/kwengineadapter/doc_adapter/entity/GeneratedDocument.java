package org.lc.kwengineadapter.doc_adapter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.common.entity.BaseEntity;

@Entity
@Table(name = "generated_documents")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedDocument extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private DocumentTemplate template;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentFormat format;

    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    private Long fileSize;

    public enum DocumentFormat {
        PDF,
        DOCX
    }

    public enum DocumentStatus {
        PENDING,
        GENERATED,
        FAILED
    }
}
