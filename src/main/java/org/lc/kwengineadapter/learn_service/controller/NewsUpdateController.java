package org.lc.kwengineadapter.learn_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.learn_service.dto.NewsUpdateRequest;
import org.lc.kwengineadapter.learn_service.dto.NewsUpdateResponse;
import org.lc.kwengineadapter.learn_service.entity.NewsUpdate;
import org.lc.kwengineadapter.learn_service.service.NewsUpdateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news-updates")
@RequiredArgsConstructor
public class NewsUpdateController {

    private final NewsUpdateService newsUpdateService;

    @PostMapping
    public ResponseEntity<ApiResponse<NewsUpdateResponse>> createNewsUpdate(@Valid @RequestBody NewsUpdateRequest request) {
        NewsUpdateResponse news = newsUpdateService.createNewsUpdate(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("News update created successfully", news));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsUpdateResponse>> getNewsUpdateById(@PathVariable Long id) {
        NewsUpdateResponse news = newsUpdateService.getNewsUpdateById(id);
        return ResponseEntity.ok(ApiResponse.success(news));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NewsUpdateResponse>>> getAllNewsUpdates() {
        List<NewsUpdateResponse> news = newsUpdateService.getAllNewsUpdates();
        return ResponseEntity.ok(ApiResponse.success(news));
    }

    @GetMapping("/published")
    public ResponseEntity<ApiResponse<List<NewsUpdateResponse>>> getPublishedNewsUpdates() {
        List<NewsUpdateResponse> news = newsUpdateService.getPublishedNewsUpdates();
        return ResponseEntity.ok(ApiResponse.success(news));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<NewsUpdateResponse>>> getNewsUpdatesByCategory(
            @PathVariable NewsUpdate.NewsCategory category) {
        List<NewsUpdateResponse> news = newsUpdateService.getNewsUpdatesByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(news));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NewsUpdateResponse>> updateNewsUpdate(
            @PathVariable Long id,
            @Valid @RequestBody NewsUpdateRequest request) {
        NewsUpdateResponse news = newsUpdateService.updateNewsUpdate(id, request);
        return ResponseEntity.ok(ApiResponse.success("News update updated successfully", news));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<NewsUpdateResponse>> publishNewsUpdate(@PathVariable Long id) {
        NewsUpdateResponse news = newsUpdateService.publishNewsUpdate(id);
        return ResponseEntity.ok(ApiResponse.success("News update published successfully", news));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNewsUpdate(@PathVariable Long id) {
        newsUpdateService.deleteNewsUpdate(id);
        return ResponseEntity.ok(ApiResponse.success("News update deleted successfully", null));
    }
}
