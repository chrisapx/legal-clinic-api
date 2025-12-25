package org.lc.kwengineadapter.doc_adapter.repository;

import org.lc.kwengineadapter.doc_adapter.entity.DocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, Long> {
    List<DocumentTemplate> findByCategory(String category);
    List<DocumentTemplate> findByActive(Boolean active);
    List<DocumentTemplate> findByCategoryAndActive(String category, Boolean active);
}
