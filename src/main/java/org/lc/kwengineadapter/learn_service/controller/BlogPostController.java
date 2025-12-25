package org.lc.kwengineadapter.learn_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.learn_service.dto.BlogPostRequest;
import org.lc.kwengineadapter.learn_service.dto.BlogPostResponse;
import org.lc.kwengineadapter.learn_service.service.BlogPostService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog-posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BlogPostController {

    private final BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<ApiResponse<BlogPostResponse>> createBlogPost(@Valid @RequestBody BlogPostRequest request) {
        BlogPostResponse post = blogPostService.createBlogPost(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Blog post created successfully", post));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogPostResponse>> getBlogPostById(@PathVariable Long id) {
        BlogPostResponse post = blogPostService.getBlogPostById(id);
        return ResponseEntity.ok(ApiResponse.success(post));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BlogPostResponse>>> getAllBlogPosts() {
        List<BlogPostResponse> posts = blogPostService.getAllBlogPosts();
        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @GetMapping("/published")
    public ResponseEntity<ApiResponse<List<BlogPostResponse>>> getPublishedBlogPosts() {
        List<BlogPostResponse> posts = blogPostService.getPublishedBlogPosts();
        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<BlogPostResponse>>> getBlogPostsByCategory(@PathVariable String category) {
        List<BlogPostResponse> posts = blogPostService.getBlogPostsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogPostResponse>> updateBlogPost(
            @PathVariable Long id,
            @Valid @RequestBody BlogPostRequest request) {
        BlogPostResponse post = blogPostService.updateBlogPost(id, request);
        return ResponseEntity.ok(ApiResponse.success("Blog post updated successfully", post));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<BlogPostResponse>> publishBlogPost(@PathVariable Long id) {
        BlogPostResponse post = blogPostService.publishBlogPost(id);
        return ResponseEntity.ok(ApiResponse.success("Blog post published successfully", post));
    }

    @PatchMapping("/{id}/unpublish")
    public ResponseEntity<ApiResponse<BlogPostResponse>> unpublishBlogPost(@PathVariable Long id) {
        BlogPostResponse post = blogPostService.unpublishBlogPost(id);
        return ResponseEntity.ok(ApiResponse.success("Blog post unpublished successfully", post));
    }

    @GetMapping("/published/paginated")
    public ResponseEntity<ApiResponse<Page<BlogPostResponse>>> getPublishedBlogPostsPaginated(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BlogPostResponse> posts = blogPostService.getPublishedBlogPostsPaginated(search, category, page, size);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @GetMapping("/all/paginated")
    public ResponseEntity<ApiResponse<Page<BlogPostResponse>>> getAllBlogPostsPaginated(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BlogPostResponse> posts = blogPostService.getAllBlogPostsPaginated(search, page, size);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBlogPost(@PathVariable Long id) {
        blogPostService.deleteBlogPost(id);
        return ResponseEntity.ok(ApiResponse.success("Blog post deleted successfully", null));
    }
}
