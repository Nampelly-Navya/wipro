package com.musiclibrary.admin.service.impl;

import com.musiclibrary.admin.dto.AdminDTO;
import com.musiclibrary.admin.dto.AdminLoginRequestDTO;
import com.musiclibrary.admin.dto.AdminLoginResponseDTO;
import com.musiclibrary.admin.dto.LoginNotificationDTO;
import com.musiclibrary.admin.dto.WelcomeEmailDTO;
import com.musiclibrary.admin.entity.Admin;
import com.musiclibrary.admin.exception.AuthenticationException;
import com.musiclibrary.admin.exception.DuplicateResourceException;
import com.musiclibrary.admin.exception.ResourceNotFoundException;
import com.musiclibrary.admin.feign.NotificationServiceClient;
import com.musiclibrary.admin.repository.AdminRepository;
import com.musiclibrary.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminServiceImpl implements AdminService {
    
    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;
    private final NotificationServiceClient notificationServiceClient;
    
    @Override
    public AdminDTO register(AdminDTO adminDTO) {
        log.info("Registering admin: {}", adminDTO.getUsername());
        
        if (adminRepository.existsByUsername(adminDTO.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (adminRepository.existsByEmail(adminDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        
        Admin admin = modelMapper.map(adminDTO, Admin.class);
        Admin saved = adminRepository.save(admin);
        
        // Send welcome email
        try {
            log.info("Sending welcome email to admin: {}", adminDTO.getEmail());
            WelcomeEmailDTO welcomeEmail = new WelcomeEmailDTO();
            welcomeEmail.setToEmail(adminDTO.getEmail());
            welcomeEmail.setName(adminDTO.getAdminName());
            welcomeEmail.setUsername(adminDTO.getUsername());
            welcomeEmail.setUserType("ADMIN");
            notificationServiceClient.sendWelcomeEmail(welcomeEmail);
            log.info("Welcome email sent successfully");
        } catch (Exception e) {
            log.warn("Failed to send welcome email: {}", e.getMessage());
        }
        
        AdminDTO response = modelMapper.map(saved, AdminDTO.class);
        response.setPassword(null);
        return response;
    }
    
    @Override
    public AdminLoginResponseDTO login(AdminLoginRequestDTO request) {
        log.info("Admin login attempt: {}", request.getUsername());
        
        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));
        
        if (!admin.getPassword().equals(request.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
        
        if (admin.getAdminStatus() != Admin.AdminStatus.ACTIVE) {
            throw new AuthenticationException("Admin account is inactive");
        }
        
        // Send login notification email
        try {
            log.info("Sending login notification to admin: {}", admin.getEmail());
            LoginNotificationDTO loginNotification = new LoginNotificationDTO();
            loginNotification.setToEmail(admin.getEmail());
            loginNotification.setName(admin.getAdminName());
            loginNotification.setUsername(admin.getUsername());
            loginNotification.setUserType("ADMIN");
            notificationServiceClient.sendLoginNotification(loginNotification);
            log.info("Login notification sent");
        } catch (Exception e) {
            log.warn("Failed to send login notification: {}", e.getMessage());
        }
        
        return new AdminLoginResponseDTO(admin.getAdminId(), admin.getAdminName(), admin.getUsername());
    }
    
    @Override
    @Transactional(readOnly = true)
    public AdminDTO getAdminById(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin", "adminId", adminId));
        AdminDTO dto = modelMapper.map(admin, AdminDTO.class);
        dto.setPassword(null);
        return dto;
    }
}

