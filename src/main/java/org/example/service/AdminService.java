package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.GetProfileRequest;
import org.example.dto.request.SetPermissionsRequest;
import org.example.dto.response.GetProfileResponse;
import org.example.entity.Permission;
import org.example.entity.User;
import org.example.entity.UserPermission;
import org.example.repository.PermissionRepository;
import org.example.repository.UserPermissionRepository;
import org.example.repository.UserRepository;
import org.example.security.JwtAuthFilter;
import org.example.skills.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final UserPermissionRepository userPermissionRepository;

    @Transactional(readOnly = true)
    public List<String> getUserPermissions(Long userId) {

        List<Long> permIds = userPermissionRepository.findByUserId(userId)
                .stream()
                .map(UserPermission::getPermissionId)
                .toList();

        if (permIds.isEmpty()) return List.of();

        return permissionRepository.findAllById(permIds)
                .stream()
                .map(Permission::getCode)
                .toList();
    }

    @Transactional
    public void setUserPermissions(Long userId, List<String> permissions) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User bulunamadı"));

        if (permissions == null || permissions.isEmpty()) return;

        var existing = userPermissionRepository.findByUserId(userId);
        var existingPermIds = existing.stream()
                .map(UserPermission::getPermissionId)
                .collect(java.util.stream.Collectors.toSet());

        for (String code : permissions) {
            Permission p = permissionRepository.findByCode(code)
                    .orElseThrow(() -> new RuntimeException("Permission yok: " + code));

            if (existingPermIds.contains(p.getId())) continue; // duplicate engel

            UserPermission up = new UserPermission();
            up.setUserId(userId);
            up.setPermissionId(p.getId());
            userPermissionRepository.save(up);
        }
    }


    @Transactional(readOnly = true)
    public List<User> getAllProfiles() {

        Long companyId = AuthUtil.getCompanyId();

        return userRepository.findAllByCompanyId(companyId);
    }
}

