package org.lc.kwengineadapter.identity_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.identity_service.dto.RoleRequest;
import org.lc.kwengineadapter.identity_service.dto.RoleResponse;
import org.lc.kwengineadapter.identity_service.entity.Permission;
import org.lc.kwengineadapter.identity_service.entity.Role;
import org.lc.kwengineadapter.identity_service.repository.PermissionRepository;
import org.lc.kwengineadapter.identity_service.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);
        role.setActive(true);

        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(
                permissionRepository.findAllById(request.getPermissionIds())
            );
            role.setPermissions(permissions);
        }

        Role savedRole = roleRepository.save(role);
        return RoleResponse.fromEntity(savedRole);
    }

    @Transactional(readOnly = true)
    public RoleResponse getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        return RoleResponse.fromEntity(role);
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(RoleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public RoleResponse updateRole(Long id, RoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));

        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setIsDefault(request.getIsDefault() != null ? request.getIsDefault() : false);

        if (request.getPermissionIds() != null) {
            Set<Permission> permissions = new HashSet<>(
                permissionRepository.findAllById(request.getPermissionIds())
            );
            role.setPermissions(permissions);
        }

        Role updatedRole = roleRepository.save(role);
        return RoleResponse.fromEntity(updatedRole);
    }

    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        roleRepository.delete(role);
    }

    @Transactional
    public RoleResponse assignPermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));

        Set<Permission> permissions = new HashSet<>(
            permissionRepository.findAllById(permissionIds)
        );
        role.setPermissions(permissions);

        Role updatedRole = roleRepository.save(role);
        return RoleResponse.fromEntity(updatedRole);
    }
}
