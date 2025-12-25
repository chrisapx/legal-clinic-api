package org.lc.kwengineadapter.learn_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.learn_service.entity.NewsUpdate;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsUpdateResponse {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private NewsUpdate.NewsCategory category;
    private String summary;
    private String imageUrl;
    private String sourceUrl;
    private Boolean published;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NewsUpdateResponse fromEntity(NewsUpdate news) {
        return new NewsUpdateResponse(
                news.getId(),
                news.getTitle(),
                news.getContent(),
                news.getAuthorId(),
                news.getCategory(),
                news.getSummary(),
                news.getImageUrl(),
                news.getSourceUrl(),
                news.getPublished(),
                news.getViewCount(),
                news.getCreatedAt(),
                news.getUpdatedAt()
        );
    }
}
