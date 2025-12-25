package org.lc.kwengineadapter.identity_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.identity_service.entity.Permission;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String grouping;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PermissionResponse fromEntity(Permission permission) {
        PermissionResponse response = new PermissionResponse();
        response.setId(permission.getId());
        response.setCode(permission.getCode());
        response.setName(permission.getName());
        response.setDescription(permission.getDescription());
        response.setGrouping(permission.getGrouping());
        response.setActive(permission.getActive());
        response.setCreatedAt(permission.getCreatedAt());
        response.setUpdatedAt(permission.getUpdatedAt());
        return response;
    }
}
