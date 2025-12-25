package org.lc.kwengineadapter.identity_service.service;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.exception.ResourceNotFoundException;
import org.lc.kwengineadapter.identity_service.dto.PermissionResponse;
import org.lc.kwengineadapter.identity_service.entity.Permission;
import org.lc.kwengineadapter.identity_service.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public PermissionResponse getPermissionById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", "id", id));
        return PermissionResponse.fromEntity(permission);
    }

    @Transactional(readOnly = true)
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(PermissionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PermissionResponse> getPermissionsByGroup(String grouping) {
        return permissionRepository.findByGrouping(grouping).stream()
                .map(PermissionResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
