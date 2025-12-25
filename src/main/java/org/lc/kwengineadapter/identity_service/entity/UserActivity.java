package org.lc.kwengineadapter.identity_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.common.entity.BaseEntity;

@Entity
@Table(name = "user_activities")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String action; // LOGIN, LOGOUT, CREATE_BLOG, UPDATE_BLOG, DELETE_BLOG, etc.

    @Column(nullable = false)
    private String entityType; // USER, BLOG, ROLE, etc.

    @Column
    private Long entityId;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column
    private String ipAddress;

    @Column
    private String userAgent;
}
