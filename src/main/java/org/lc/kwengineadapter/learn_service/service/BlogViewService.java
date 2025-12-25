package org.lc.kwengineadapter.learn_service.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.identity_service.entity.User;
import org.lc.kwengineadapter.identity_service.repository.UserRepository;
import org.lc.kwengineadapter.learn_service.entity.BlogPost;
import org.lc.kwengineadapter.learn_service.entity.BlogView;
import org.lc.kwengineadapter.learn_service.repository.BlogPostRepository;
import org.lc.kwengineadapter.learn_service.repository.BlogViewRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BlogViewService {

    private final BlogViewRepository blogViewRepository;
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;

    @Async
    @Transactional
    public void trackView(Long postId, HttpServletRequest request) {
        trackView(postId, request, null);
    }

    @Async
    @Transactional
    public void trackView(Long postId, HttpServletRequest request, Integer timeSpent) {
        BlogPost blogPost = blogPostRepository.findById(postId).orElse(null);
        if (blogPost == null) {
            return;
        }

        String ipAddress = getClientIpAddress(request);

        // Check if this IP has viewed this post in the last 24 hours
        LocalDateTime oneDayAgo = LocalDateTime.now().minusHours(24);
        boolean recentViewExists = blogViewRepository.existsByBlogPostIdAndIpAddressAndCreatedAtAfter(
                postId, ipAddress, oneDayAgo);

        // Only create a new view if no recent view exists from this IP
        if (!recentViewExists) {
            BlogView view = new BlogView();
            view.setBlogPost(blogPost);
            view.setIpAddress(ipAddress);
            view.setUserAgent(request.getHeader("User-Agent"));
            view.setReferrer(request.getHeader("Referer"));
            view.setTimeSpent(timeSpent);

            // Get authenticated user if available
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
                userRepository.findByEmail(authentication.getName()).ifPresent(view::setUser);
            }

            blogViewRepository.save(view);
        } else if (timeSpent != null && timeSpent > 0) {
            // If view exists but we have time spent data, update the existing view
            blogViewRepository.findTopByBlogPostIdAndIpAddressOrderByCreatedAtDesc(postId, ipAddress)
                    .ifPresent(existingView -> {
                        // Only update if the new time is greater (user spent more time)
                        if (existingView.getTimeSpent() == null || timeSpent > existingView.getTimeSpent()) {
                            existingView.setTimeSpent(timeSpent);
                            blogViewRepository.save(existingView);
                        }
                    });
        }
    }

    @Transactional(readOnly = true)
    public Long getViewCount(Long postId) {
        return blogViewRepository.countByBlogPostId(postId);
    }

    @Transactional(readOnly = true)
    public Long getUniqueViewCount(Long postId) {
        return blogViewRepository.countUniqueViewsByPost(postId);
    }

    @Transactional(readOnly = true)
    public Long getViewsSince(Long postId, LocalDateTime since) {
        return blogViewRepository.countViewsSince(postId, since);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
