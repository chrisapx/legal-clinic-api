package org.lc.kwengineadapter.identity_service.controller;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.identity_service.dto.ActivityResponse;
import org.lc.kwengineadapter.identity_service.service.UserActivityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserActivityController {

    private final UserActivityService userActivityService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<ActivityResponse>>> getUserActivities(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ActivityResponse> activities = userActivityService.getUserActivities(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(activities));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ActivityResponse>>> getAllActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ActivityResponse> activities = userActivityService.getAllActivities(pageable);
        return ResponseEntity.ok(ApiResponse.success(activities));
    }
}
