package org.lc.kwengineadapter.identity_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {

    @NotBlank(message = "Role name is required")
    private String name;

    private String description;

    private Boolean isDefault = false;

    private Set<Long> permissionIds;
}
