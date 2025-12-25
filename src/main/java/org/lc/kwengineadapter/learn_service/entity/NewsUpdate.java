package org.lc.kwengineadapter.learn_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.common.entity.BaseEntity;

@Entity
@Table(name = "news_updates")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewsUpdate extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Long authorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NewsCategory category;

    @Column(columnDefinition = "TEXT")
    private String summary;

    private String imageUrl;

    private String sourceUrl;

    @Column(nullable = false)
    private Boolean published = false;

    private Integer viewCount = 0;

    public enum NewsCategory {
        LEGAL_UPDATES,
        REGULATORY_CHANGES,
        COURT_DECISIONS,
        INDUSTRY_NEWS,
        GENERAL
    }
}
