package org.lc.kwengineadapter.doc_adapter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.doc_adapter.entity.DocumentTemplate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Format is required")
    private DocumentTemplate.TemplateFormat format;

    private String version;
}
