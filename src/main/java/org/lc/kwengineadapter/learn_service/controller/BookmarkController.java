package org.lc.kwengineadapter.learn_service.controller;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.learn_service.dto.BlogPostResponse;
import org.lc.kwengineadapter.learn_service.service.BookmarkService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> addBookmark(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestParam(required = false) String notes) {
        bookmarkService.addBookmark(userId, postId, notes);
        return ResponseEntity.ok(ApiResponse.success("Post bookmarked successfully", null));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> removeBookmark(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        bookmarkService.removeBookmark(userId, postId);
        return ResponseEntity.ok(ApiResponse.success("Bookmark removed successfully", null));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<BlogPostResponse>>> getUserBookmarks(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPostResponse> bookmarks = bookmarkService.getUserBookmarks(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(bookmarks));
    }

    @GetMapping("/check/{postId}")
    public ResponseEntity<ApiResponse<Boolean>> isBookmarked(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        boolean isBookmarked = bookmarkService.isBookmarked(userId, postId);
        return ResponseEntity.ok(ApiResponse.success(isBookmarked));
    }
}
