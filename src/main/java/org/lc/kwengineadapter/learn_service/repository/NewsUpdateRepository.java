package org.lc.kwengineadapter.learn_service.repository;

import org.lc.kwengineadapter.learn_service.entity.NewsUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsUpdateRepository extends JpaRepository<NewsUpdate, Long> {
    List<NewsUpdate> findByPublished(Boolean published);
    List<NewsUpdate> findByCategory(NewsUpdate.NewsCategory category);
    List<NewsUpdate> findByAuthorId(Long authorId);
    List<NewsUpdate> findByPublishedOrderByCreatedAtDesc(Boolean published);
}
