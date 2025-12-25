package org.lc.kwengineadapter.learn_service.repository;

import org.lc.kwengineadapter.identity_service.entity.User;
import org.lc.kwengineadapter.learn_service.entity.BlogPost;
import org.lc.kwengineadapter.learn_service.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Page<Bookmark> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Optional<Bookmark> findByUserAndBlogPost(User user, BlogPost blogPost);

    boolean existsByUserAndBlogPost(User user, BlogPost blogPost);

    Long countByBlogPost(BlogPost blogPost);

    void deleteByUserAndBlogPost(User user, BlogPost blogPost);
}
