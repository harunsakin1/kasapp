package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.SetPermissionsRequest;
import org.example.entity.User;
import org.example.exception.ErrorType;
import org.example.exception.KasappException;
import org.example.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS
        }
)
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{userId}/permissions")
    public List<String> getUserPermissions(@PathVariable("userId") Long userId) {
        return adminService.getUserPermissions(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{userId}/permissions")
    public ResponseEntity<Void> setUserPermissions(
            @PathVariable Long userId,
            @RequestBody SetPermissionsRequest req) {

        if (req == null || req.permissions() == null) {
            throw new KasappException(ErrorType.PERMISSION_LIST_CANNOT_BE_EMPTY);
        }

        adminService.replaceUserPermissions(userId, req.permissions());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profiles")
    public List<User> getAllProfiles() {
        return adminService.getAllProfiles();
    }

    // 🔥 SİLME / PASİF YAPMA
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        adminService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }
}
