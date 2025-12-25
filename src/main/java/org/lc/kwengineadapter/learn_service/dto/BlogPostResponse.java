package org.lc.kwengineadapter.learn_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.learn_service.entity.BlogPost;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostResponse {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String category;
    private String summary;
    private String imageUrl;
    private Boolean published;
    private Integer viewCount;
    private String tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BlogPostResponse fromEntity(BlogPost post) {
        return new BlogPostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthorId(),
                post.getCategory(),
                post.getSummary(),
                post.getImageUrl(),
                post.getPublished(),
                post.getViewCount(),
                post.getTags(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
