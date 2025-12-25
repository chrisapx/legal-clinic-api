package org.lc.kwengineadapter.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lc.kwengineadapter.identity_service.entity.Permission;
import org.lc.kwengineadapter.identity_service.entity.Role;
import org.lc.kwengineadapter.identity_service.entity.User;
import org.lc.kwengineadapter.identity_service.repository.PermissionRepository;
import org.lc.kwengineadapter.identity_service.repository.RoleRepository;
import org.lc.kwengineadapter.identity_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Initializing database with default data...");

        // Create permissions
        List<Permission> permissions = createPermissions();

        // Create default roles
        createDefaultRoles(permissions);

        // Create super admin user if not exists
        createSuperAdminUser();

        log.info("Database initialization completed.");
    }

    private List<Permission> createPermissions() {
        List<Permission> permissionsList = Arrays.asList(
                // User permissions
                createPermission("READ_USER", "View Users", "View user details", "USER"),
                createPermission("CREATE_USER", "Create User", "Create new users", "USER"),
                createPermission("UPDATE_USER", "Update User", "Update user information", "USER"),
                createPermission("DELETE_USER", "Delete User", "Delete users", "USER"),

                // Role permissions
                createPermission("READ_ROLE", "View Roles", "View role details", "ROLE"),
                createPermission("CREATE_ROLE", "Create Role", "Create new roles", "ROLE"),
                createPermission("UPDATE_ROLE", "Update Role", "Update role information", "ROLE"),
                createPermission("DELETE_ROLE", "Delete Role", "Delete roles", "ROLE"),

                // Permission management
                createPermission("READ_PERMISSION", "View Permissions", "View permission details", "PERMISSION"),
                createPermission("ASSIGN_PERMISSION", "Assign Permissions", "Assign permissions to roles", "PERMISSION"),

                // Blog permissions
                createPermission("READ_BLOG", "View Blogs", "View blog posts", "BLOG"),
                createPermission("CREATE_BLOG", "Create Blog", "Create new blog posts", "BLOG"),
                createPermission("UPDATE_BLOG", "Update Blog", "Update blog posts", "BLOG"),
                createPermission("DELETE_BLOG", "Delete Blog", "Delete blog posts", "BLOG"),
                createPermission("PUBLISH_BLOG", "Publish Blog", "Publish/unpublish blog posts", "BLOG"),

                // Activity permissions
                createPermission("READ_ACTIVITY", "View Activities", "View user activities", "ACTIVITY")
        );

        permissionsList.forEach(permission -> {
            if (!permissionRepository.existsByCode(permission.getCode())) {
                permissionRepository.save(permission);
                log.info("Created permission: {}", permission.getCode());
            }
        });

        return permissionRepository.findAll();
    }

    private void createDefaultRoles(List<Permission> allPermissions) {
        // Create SUPER_ADMIN role with all permissions
        if (!roleRepository.existsByName("SUPER_ADMIN")) {
            Role superAdmin = new Role();
            superAdmin.setName("SUPER_ADMIN");
            superAdmin.setDescription("Super Administrator with all permissions");
            superAdmin.setIsDefault(true);
            superAdmin.setActive(true);
            superAdmin.setPermissions(new HashSet<>(allPermissions));
            roleRepository.save(superAdmin);
            log.info("Created SUPER_ADMIN role with all permissions");
        }

        // Create USER role with limited permissions
        if (!roleRepository.existsByName("USER")) {
            Role userRole = new Role();
            userRole.setName("USER");
            userRole.setDescription("Regular user with limited permissions");
            userRole.setIsDefault(true);
            userRole.setActive(true);

            // Users can only read blogs
            HashSet<Permission> userPermissions = new HashSet<>();
            permissionRepository.findByCode("READ_BLOG").ifPresent(userPermissions::add);

            userRole.setPermissions(userPermissions);
            roleRepository.save(userRole);
            log.info("Created USER role with limited permissions");
        }
    }

    private void createSuperAdminUser() {
        if (!userRepository.existsByEmail("admin@legalconnect.com")) {
            Role superAdminRole = roleRepository.findByName("SUPER_ADMIN")
                    .orElseThrow(() -> new RuntimeException("SUPER_ADMIN role not found"));

            User admin = new User();
            admin.setFirstName("Super");
            admin.setLastName("Admin");
            admin.setEmail("admin@legalconnect.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setPhoneNumber("+1234567890");
            admin.setRole(superAdminRole);
            admin.setActive(true);
            admin.setEmailVerified(true);

            userRepository.save(admin);
            log.info("Created super admin user: admin@legalconnect.com / Admin@123");
        }
    }

    private Permission createPermission(String code, String name, String description, String grouping) {
        Permission permission = new Permission();
        permission.setCode(code);
        permission.setName(name);
        permission.setDescription(description);
        permission.setGrouping(grouping);
        permission.setActive(true);
        return permission;
    }
}
