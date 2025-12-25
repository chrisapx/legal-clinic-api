package org.lc.kwengineadapter.learn_service.repository;

import org.lc.kwengineadapter.learn_service.entity.BlogPost;
import org.lc.kwengineadapter.learn_service.entity.BlogView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BlogViewRepository extends JpaRepository<BlogView, Long> {

    Long countByBlogPost(BlogPost blogPost);

    Long countByBlogPostId(Long blogPostId);

    @Query("SELECT COUNT(v) FROM BlogView v WHERE v.blogPost.id = :postId AND v.createdAt >= :since")
    Long countViewsSince(@Param("postId") Long postId, @Param("since") LocalDateTime since);

    @Query("SELECT COUNT(DISTINCT v.ipAddress) FROM BlogView v WHERE v.blogPost.id = :postId")
    Long countUniqueViewsByPost(@Param("postId") Long postId);

    // Check if a view exists from a specific IP within a time window
    boolean existsByBlogPostIdAndIpAddressAndCreatedAtAfter(Long blogPostId, String ipAddress, LocalDateTime createdAt);

    // Find the most recent view from a specific IP for a post
    Optional<BlogView> findTopByBlogPostIdAndIpAddressOrderByCreatedAtDesc(Long blogPostId, String ipAddress);
}
