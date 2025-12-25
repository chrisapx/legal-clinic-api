package org.lc.kwengineadapter.learn_service.repository;

import org.lc.kwengineadapter.learn_service.entity.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByPublished(Boolean published);
    List<BlogPost> findByCategory(String category);
    List<BlogPost> findByAuthorId(Long authorId);
    List<BlogPost> findByPublishedOrderByCreatedAtDesc(Boolean published);

    // Pagination methods
    Page<BlogPost> findByPublished(Boolean published, Pageable pageable);
    Page<BlogPost> findByPublishedAndCategory(Boolean published, String category, Pageable pageable);
    Page<BlogPost> findByPublishedAndTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            Boolean published, String titleSearch, String contentSearch, Pageable pageable);
    Page<BlogPost> findByPublishedAndCategoryAndTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            Boolean published, String category, String titleSearch, String contentSearch, Pageable pageable);
    Page<BlogPost> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String titleSearch, String contentSearch, Pageable pageable);
}
