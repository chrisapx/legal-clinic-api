package org.lc.kwengineadapter.identity_service.controller;

import lombok.RequiredArgsConstructor;
import org.lc.kwengineadapter.common.dto.ApiResponse;
import org.lc.kwengineadapter.identity_service.dto.PermissionResponse;
import org.lc.kwengineadapter.identity_service.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponse>> getPermissionById(@PathVariable Long id) {
        PermissionResponse permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(ApiResponse.success(permission));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermissions() {
        List<PermissionResponse> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(ApiResponse.success(permissions));
    }

    @GetMapping("/by-group/{grouping}")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getPermissionsByGroup(@PathVariable String grouping) {
        List<PermissionResponse> permissions = permissionService.getPermissionsByGroup(grouping);
        return ResponseEntity.ok(ApiResponse.success(permissions));
    }
}
