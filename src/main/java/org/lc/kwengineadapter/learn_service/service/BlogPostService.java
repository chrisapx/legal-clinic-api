package org.lc.kwengineadapter.learn_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.learn_service.dto.BlogPostRequest;
import org.lc.kwengineadapter.learn_service.dto.BlogPostResponse;
import org.lc.kwengineadapter.learn_service.entity.BlogPost;
import org.lc.kwengineadapter.learn_service.repository.BlogPostRepository;
import org.lc.kwengineadapter.learn_service.repository.BlogViewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final BlogViewRepository blogViewRepository;

    @Transactional
    public BlogPostResponse createBlogPost(BlogPostRequest request) {
        BlogPost post = new BlogPost();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthorId(request.getAuthorId());
        post.setCategory(request.getCategory());
        post.setSummary(request.getSummary());
        post.setImageUrl(request.getImageUrl());
        post.setTags(request.getTags());
        post.setPublished(false);
        post.setViewCount(0);

        BlogPost savedPost = blogPostRepository.save(post);
        return BlogPostResponse.fromEntity(savedPost);
    }

    public BlogPostResponse getBlogPostById(Long id) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost", "id", id));
        return toResponseWithViewCount(post);
    }

    public List<BlogPostResponse> getAllBlogPosts() {
        return blogPostRepository.findAll().stream()
                .map(this::toResponseWithViewCount)
                .collect(Collectors.toList());
    }

    public List<BlogPostResponse> getPublishedBlogPosts() {
        return blogPostRepository.findByPublishedOrderByCreatedAtDesc(true).stream()
                .map(this::toResponseWithViewCount)
                .collect(Collectors.toList());
    }

    public List<BlogPostResponse> getBlogPostsByCategory(String category) {
        return blogPostRepository.findByCategory(category).stream()
                .map(this::toResponseWithViewCount)
                .collect(Collectors.toList());
    }

    private BlogPostResponse toResponseWithViewCount(BlogPost post) {
        BlogPostResponse response = BlogPostResponse.fromEntity(post);
        Long viewCount = blogViewRepository.countByBlogPostId(post.getId());
        response.setViewCount(viewCount != null ? viewCount.intValue() : 0);
        return response;
    }

    @Transactional
    public BlogPostResponse updateBlogPost(Long id, BlogPostRequest request) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost", "id", id));

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setCategory(request.getCategory());
        post.setSummary(request.getSummary());
        post.setImageUrl(request.getImageUrl());
        post.setTags(request.getTags());

        BlogPost updatedPost = blogPostRepository.save(post);
        return BlogPostResponse.fromEntity(updatedPost);
    }

    @Transactional
    public BlogPostResponse publishBlogPost(Long id) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost", "id", id));
        post.setPublished(true);
        BlogPost publishedPost = blogPostRepository.save(post);
        return BlogPostResponse.fromEntity(publishedPost);
    }

    @Transactional
    public BlogPostResponse unpublishBlogPost(Long id) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost", "id", id));
        post.setPublished(false);
        BlogPost unpublishedPost = blogPostRepository.save(post);
        return BlogPostResponse.fromEntity(unpublishedPost);
    }

    @Transactional
    public void deleteBlogPost(Long id) {
        if (!blogPostRepository.existsById(id)) {
            throw new ResourceNotFoundException("BlogPost", "id", id);
        }
        blogPostRepository.deleteById(id);
    }

    public Page<BlogPostResponse> getPublishedBlogPostsPaginated(String search, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BlogPost> posts;

        if (search != null && !search.trim().isEmpty() && category != null && !category.trim().isEmpty() && !category.equals("All")) {
            // Search with category
            posts = blogPostRepository.findByPublishedAndCategoryAndTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                    true, category, search, search, pageable);
        } else if (search != null && !search.trim().isEmpty()) {
            // Search only
            posts = blogPostRepository.findByPublishedAndTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                    true, search, search, pageable);
        } else if (category != null && !category.trim().isEmpty() && !category.equals("All")) {
            // Category only
            posts = blogPostRepository.findByPublishedAndCategory(true, category, pageable);
        } else {
            // All posts
            posts = blogPostRepository.findByPublished(true, pageable);
        }

        return posts.map(this::toResponseWithViewCount);
    }

    public Page<BlogPostResponse> getAllBlogPostsPaginated(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BlogPost> posts;

        if (search != null && !search.trim().isEmpty()) {
            posts = blogPostRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                    search, search, pageable);
        } else {
            posts = blogPostRepository.findAll(pageable);
        }

        return posts.map(this::toResponseWithViewCount);
    }
}
