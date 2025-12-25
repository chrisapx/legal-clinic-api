package org.lc.kwengineadapter.identity_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lc.kwengineadapter.identity_service.entity.Role;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private Long id;
    private String name;
    private String description;
    private Boolean isDefault;
    private Boolean active;
    private Set<PermissionResponse> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RoleResponse fromEntity(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setIsDefault(role.getIsDefault());
        response.setActive(role.getActive());
        response.setCreatedAt(role.getCreatedAt());
        response.setUpdatedAt(role.getUpdatedAt());

        if (role.getPermissions() != null) {
            response.setPermissions(
                role.getPermissions().stream()
                    .map(PermissionResponse::fromEntity)
                    .collect(Collectors.toSet())
            );
        }

        return response;
    }
}
