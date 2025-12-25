package org.lc.kwengineadapter.doc_adapter.repository;

import org.lc.kwengineadapter.doc_adapter.entity.GeneratedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneratedDocumentRepository extends JpaRepository<GeneratedDocument, Long> {
    List<GeneratedDocument> findByUserId(Long userId);
    List<GeneratedDocument> findByTemplateId(Long templateId);
    List<GeneratedDocument> findByStatus(GeneratedDocument.DocumentStatus status);
}
