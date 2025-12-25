package org.lc.kwengineadapter.learn_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.common.entity.BaseEntity;
import org.lc.kwengineadapter.identity_service.entity.User;

@Entity
@Table(name = "blog_views", indexes = {
    @Index(name = "idx_blog_post_id", columnList = "blog_post_id"),
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_ip_address", columnList = "ip_address")
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BlogView extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_post_id", nullable = false)
    private BlogPost blogPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // Nullable for anonymous views

    @Column(nullable = false)
    private String ipAddress;

    @Column
    private String userAgent;

    @Column
    private String referrer;

    @Column
    private Integer timeSpent; // Time spent on page in seconds
}
