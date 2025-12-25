package org.lc.kwengineadapter.learn_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.identity_service.entity.User;
import org.lc.kwengineadapter.identity_service.repository.UserRepository;
import org.lc.kwengineadapter.learn_service.dto.BlogPostResponse;
import org.lc.kwengineadapter.learn_service.entity.BlogPost;
import org.lc.kwengineadapter.learn_service.entity.Bookmark;
import org.lc.kwengineadapter.learn_service.repository.BlogPostRepository;
import org.lc.kwengineadapter.learn_service.repository.BookmarkRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addBookmark(Long userId, Long postId, String notes) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        BlogPost blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost", "id", postId));

        if (bookmarkRepository.existsByUserAndBlogPost(user, blogPost)) {
            throw new IllegalStateException("Post already bookmarked");
        }

        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setBlogPost(blogPost);
        bookmark.setNotes(notes);

        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void removeBookmark(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        BlogPost blogPost = blogPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("BlogPost", "id", postId));

        bookmarkRepository.deleteByUserAndBlogPost(user, blogPost);
    }

    @Transactional(readOnly = true)
    public Page<BlogPostResponse> getUserBookmarks(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Page<Bookmark> bookmarks = bookmarkRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        return bookmarks.map(bookmark -> BlogPostResponse.fromEntity(bookmark.getBlogPost()));
    }

    @Transactional(readOnly = true)
    public boolean isBookmarked(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElse(null);
        BlogPost blogPost = blogPostRepository.findById(postId).orElse(null);

        if (user == null || blogPost == null) {
            return false;
        }

        return bookmarkRepository.existsByUserAndBlogPost(user, blogPost);
    }

    @Transactional(readOnly = true)
    public Long getBookmarkCount(Long postId) {
        BlogPost blogPost = blogPostRepository.findById(postId).orElse(null);
        if (blogPost == null) {
            return 0L;
        }
        return bookmarkRepository.countByBlogPost(blogPost);
    }
}
