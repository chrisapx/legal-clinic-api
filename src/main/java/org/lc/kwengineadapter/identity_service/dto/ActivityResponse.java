package org.lc.kwengineadapter.identity_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.identity_service.entity.UserActivity;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String action;
    private String entityType;
    private Long entityId;
    private String details;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;

    public static ActivityResponse fromEntity(UserActivity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());

        if (activity.getUser() != null) {
            response.setUserId(activity.getUser().getId());
            response.setUserName(activity.getUser().getFirstName() + " " + activity.getUser().getLastName());
            response.setUserEmail(activity.getUser().getEmail());
        }

        response.setAction(activity.getAction());
        response.setEntityType(activity.getEntityType());
        response.setEntityId(activity.getEntityId());
        response.setDetails(activity.getDetails());
        response.setIpAddress(activity.getIpAddress());
        response.setUserAgent(activity.getUserAgent());
        response.setCreatedAt(activity.getCreatedAt());

        return response;
    }
}
