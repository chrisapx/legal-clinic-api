package org.lc.kwengineadapter.learn_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.learn_service.service.BlogViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blog-views")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BlogViewController {

    private final BlogViewService blogViewService;

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> trackView(
            @PathVariable Long postId,
            @RequestParam(required = false) Integer timeSpent,
            HttpServletRequest request) {
        blogViewService.trackView(postId, request, timeSpent);
        return ResponseEntity.ok(ApiResponse.success("View tracked successfully", null));
    }

    @GetMapping("/{postId}/count")
    public ResponseEntity<ApiResponse<Long>> getViewCount(@PathVariable Long postId) {
        Long count = blogViewService.getViewCount(postId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @GetMapping("/{postId}/unique-count")
    public ResponseEntity<ApiResponse<Long>> getUniqueViewCount(@PathVariable Long postId) {
        Long count = blogViewService.getUniqueViewCount(postId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}
