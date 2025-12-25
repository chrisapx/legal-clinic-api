package org.lc.kwengineadapter.learn_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.learn_service.entity.NewsUpdate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsUpdateRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    @NotNull(message = "Category is required")
    private NewsUpdate.NewsCategory category;

    private String summary;

    private String imageUrl;

    private String sourceUrl;
}
