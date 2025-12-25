package org.lc.kwengineadapter.doc_adapter.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.BadRequestException;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.doc_adapter.dto.DocumentGenerationRequest;
import org.lc.kwengineadapter.doc_adapter.dto.GeneratedDocumentResponse;
import org.lc.kwengineadapter.doc_adapter.entity.DocumentTemplate;
import org.lc.kwengineadapter.doc_adapter.entity.GeneratedDocument;
import org.lc.kwengineadapter.doc_adapter.repository.DocumentTemplateRepository;
import org.lc.kwengineadapter.doc_adapter.repository.GeneratedDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentGenerationService {

    private final GeneratedDocumentRepository documentRepository;
    private final DocumentTemplateRepository templateRepository;

    @Transactional
    public GeneratedDocumentResponse generateDocument(DocumentGenerationRequest request) {
        DocumentTemplate template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("DocumentTemplate", "id", request.getTemplateId()));

        if (!template.getActive()) {
            throw new BadRequestException("Template is not active");
        }

        // Create document record
        GeneratedDocument document = new GeneratedDocument();
        document.setTemplate(template);
        document.setUserId(request.getUserId());
        document.setFormat(request.getFormat());
        document.setStatus(GeneratedDocument.DocumentStatus.PENDING);

        // Generate unique filename
        String fileName = generateFileName(template.getName(), request.getFormat());
        document.setFileName(fileName);

        // For MVP, store a placeholder path
        String filePath = "/documents/" + request.getUserId() + "/" + fileName;
        document.setFilePath(filePath);

        // Store metadata as JSON string if provided
        if (request.getData() != null) {
            document.setMetadata(request.getData().toString());
        }

        try {
            // TODO: Implement actual PDF/DOCX generation using Spring AI
            // For MVP, mark as generated with placeholder
            document.setStatus(GeneratedDocument.DocumentStatus.GENERATED);
            document.setFileSize(0L);

            GeneratedDocument savedDocument = documentRepository.save(document);
            return GeneratedDocumentResponse.fromEntity(savedDocument);
        } catch (Exception e) {
            document.setStatus(GeneratedDocument.DocumentStatus.FAILED);
            documentRepository.save(document);
            throw new BadRequestException("Failed to generate document: " + e.getMessage());
        }
    }

    public GeneratedDocumentResponse getDocumentById(Long id) {
        GeneratedDocument document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GeneratedDocument", "id", id));
        return GeneratedDocumentResponse.fromEntity(document);
    }

    public List<GeneratedDocumentResponse> getDocumentsByUser(Long userId) {
        return documentRepository.findByUserId(userId).stream()
                .map(GeneratedDocumentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<GeneratedDocumentResponse> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(GeneratedDocumentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new ResourceNotFoundException("GeneratedDocument", "id", id);
        }
        // TODO: Delete physical file from storage
        documentRepository.deleteById(id);
    }

    private String generateFileName(String templateName, GeneratedDocument.DocumentFormat format) {
        String sanitizedName = templateName.replaceAll("[^a-zA-Z0-9]", "_");
        String extension = format == GeneratedDocument.DocumentFormat.PDF ? ".pdf" : ".docx";
        return sanitizedName + "_" + UUID.randomUUID() + extension;
    }
}
