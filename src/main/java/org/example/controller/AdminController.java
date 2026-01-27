package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.GetProfileRequest;
import org.example.dto.request.SetPermissionsRequest;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.service.AdminService;
import org.example.skills.AuthUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{userId}/permissions")
    public List<String> getUserPermissions(@PathVariable("userId") Long userId) {
        return adminService.getUserPermissions(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/{userId}/permissions")
    public void setUserPermissions(@PathVariable("userId") Long userId,
                                   @RequestBody SetPermissionsRequest req) {

        System.out.println(">>> SET PERMISSIONS API CALLED <<<");
        System.out.println("USER ID = " + userId);
        System.out.println("PERMS = " + (req == null ? null : req.permissions()));

        adminService.setUserPermissions(userId, req == null ? null : req.permissions());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profiles")
    public List<User> getAllProfiles() {
        return adminService.getAllProfiles();
    }
}



