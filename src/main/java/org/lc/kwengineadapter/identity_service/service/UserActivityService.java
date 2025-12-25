package org.lc.kwengineadapter.identity_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.identity_service.dto.ActivityResponse;
import org.lc.kwengineadapter.identity_service.entity.User;
import org.lc.kwengineadapter.identity_service.entity.UserActivity;
import org.lc.kwengineadapter.identity_service.repository.UserActivityRepository;
import org.lc.kwengineadapter.identity_service.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<ActivityResponse> getUserActivities(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Page<UserActivity> activities = userActivityRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        return activities.map(ActivityResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ActivityResponse> getAllActivities(Pageable pageable) {
        Page<UserActivity> activities = userActivityRepository.findAllByOrderByCreatedAtDesc(pageable);
        return activities.map(ActivityResponse::fromEntity);
    }
}
