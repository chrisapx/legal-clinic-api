package org.lc.kwengineadapter.learn_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.learn_service.dto.NewsUpdateRequest;
import org.lc.kwengineadapter.learn_service.dto.NewsUpdateResponse;
import org.lc.kwengineadapter.learn_service.entity.NewsUpdate;
import org.lc.kwengineadapter.learn_service.repository.NewsUpdateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsUpdateService {

    private final NewsUpdateRepository newsUpdateRepository;

    @Transactional
    public NewsUpdateResponse createNewsUpdate(NewsUpdateRequest request) {
        NewsUpdate news = new NewsUpdate();
        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setAuthorId(request.getAuthorId());
        news.setCategory(request.getCategory());
        news.setSummary(request.getSummary());
        news.setImageUrl(request.getImageUrl());
        news.setSourceUrl(request.getSourceUrl());
        news.setPublished(false);
        news.setViewCount(0);

        NewsUpdate savedNews = newsUpdateRepository.save(news);
        return NewsUpdateResponse.fromEntity(savedNews);
    }

    public NewsUpdateResponse getNewsUpdateById(Long id) {
        NewsUpdate news = newsUpdateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NewsUpdate", "id", id));
        return NewsUpdateResponse.fromEntity(news);
    }

    public List<NewsUpdateResponse> getAllNewsUpdates() {
        return newsUpdateRepository.findAll().stream()
                .map(NewsUpdateResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<NewsUpdateResponse> getPublishedNewsUpdates() {
        return newsUpdateRepository.findByPublishedOrderByCreatedAtDesc(true).stream()
                .map(NewsUpdateResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<NewsUpdateResponse> getNewsUpdatesByCategory(NewsUpdate.NewsCategory category) {
        return newsUpdateRepository.findByCategory(category).stream()
                .map(NewsUpdateResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public NewsUpdateResponse updateNewsUpdate(Long id, NewsUpdateRequest request) {
        NewsUpdate news = newsUpdateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NewsUpdate", "id", id));

        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setCategory(request.getCategory());
        news.setSummary(request.getSummary());
        news.setImageUrl(request.getImageUrl());
        news.setSourceUrl(request.getSourceUrl());

        NewsUpdate updatedNews = newsUpdateRepository.save(news);
        return NewsUpdateResponse.fromEntity(updatedNews);
    }

    @Transactional
    public NewsUpdateResponse publishNewsUpdate(Long id) {
        NewsUpdate news = newsUpdateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NewsUpdate", "id", id));
        news.setPublished(true);
        NewsUpdate publishedNews = newsUpdateRepository.save(news);
        return NewsUpdateResponse.fromEntity(publishedNews);
    }

    @Transactional
    public void deleteNewsUpdate(Long id) {
        if (!newsUpdateRepository.existsById(id)) {
            throw new ResourceNotFoundException("NewsUpdate", "id", id);
        }
        newsUpdateRepository.deleteById(id);
    }
}
