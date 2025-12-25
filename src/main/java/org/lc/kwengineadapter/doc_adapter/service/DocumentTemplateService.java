package org.lc.kwengineadapter.doc_adapter.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.doc_adapter.dto.TemplateRequest;
import org.lc.kwengineadapter.doc_adapter.dto.TemplateResponse;
import org.lc.kwengineadapter.doc_adapter.entity.DocumentTemplate;
import org.lc.kwengineadapter.doc_adapter.repository.DocumentTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentTemplateService {

    private final DocumentTemplateRepository templateRepository;

    @Transactional
    public TemplateResponse createTemplate(TemplateRequest request) {
        DocumentTemplate template = new DocumentTemplate();
        template.setName(request.getName());
        template.setCategory(request.getCategory());
        template.setDescription(request.getDescription());
        template.setContent(request.getContent());
        template.setFormat(request.getFormat());
        template.setVersion(request.getVersion());
        template.setActive(true);

        DocumentTemplate savedTemplate = templateRepository.save(template);
        return TemplateResponse.fromEntity(savedTemplate);
    }

    public TemplateResponse getTemplateById(Long id) {
        DocumentTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentTemplate", "id", id));
        return TemplateResponse.fromEntity(template);
    }

    public List<TemplateResponse> getAllTemplates() {
        return templateRepository.findAll().stream()
                .map(TemplateResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TemplateResponse> getTemplatesByCategory(String category) {
        return templateRepository.findByCategory(category).stream()
                .map(TemplateResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TemplateResponse> getActiveTemplates() {
        return templateRepository.findByActive(true).stream()
                .map(TemplateResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public TemplateResponse updateTemplate(Long id, TemplateRequest request) {
        DocumentTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentTemplate", "id", id));

        template.setName(request.getName());
        template.setCategory(request.getCategory());
        template.setDescription(request.getDescription());
        template.setContent(request.getContent());
        template.setFormat(request.getFormat());
        template.setVersion(request.getVersion());

        DocumentTemplate updatedTemplate = templateRepository.save(template);
        return TemplateResponse.fromEntity(updatedTemplate);
    }

    @Transactional
    public void deleteTemplate(Long id) {
        if (!templateRepository.existsById(id)) {
            throw new ResourceNotFoundException("DocumentTemplate", "id", id);
        }
        templateRepository.deleteById(id);
    }

    @Transactional
    public TemplateResponse deactivateTemplate(Long id) {
        DocumentTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentTemplate", "id", id));
        template.setActive(false);
        DocumentTemplate updatedTemplate = templateRepository.save(template);
        return TemplateResponse.fromEntity(updatedTemplate);
    }
}
