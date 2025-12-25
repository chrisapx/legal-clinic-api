package org.lc.kwengineadapter.learn_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.common.entity.BaseEntity;
import org.lc.kwengineadapter.identity_service.entity.User;

@Entity
@Table(name = "bookmarks",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "blog_post_id"}),
    indexes = {
        @Index(name = "idx_user_bookmarks", columnList = "user_id"),
        @Index(name = "idx_post_bookmarks", columnList = "blog_post_id")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_post_id", nullable = false)
    private BlogPost blogPost;

    @Column
    private String notes; // User's personal notes about the bookmark
}
