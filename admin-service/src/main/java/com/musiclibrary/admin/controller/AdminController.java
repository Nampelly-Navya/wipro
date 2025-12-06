package com.musiclibrary.admin.controller;

import com.musiclibrary.admin.dto.AdminDTO;
import com.musiclibrary.admin.dto.AdminLoginRequestDTO;
import com.musiclibrary.admin.dto.AdminLoginResponseDTO;
import com.musiclibrary.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
@Tag(name = "Admin Authentication", description = "Admin registration and login APIs")
public class AdminController {
    
    private final AdminService adminService;
    
    @PostMapping("/register")
    @Operation(summary = "Register new admin")
    public ResponseEntity<AdminDTO> register(@Valid @RequestBody AdminDTO adminDTO) {
        return new ResponseEntity<>(adminService.register(adminDTO), HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    @Operation(summary = "Admin login")
    public ResponseEntity<AdminLoginResponseDTO> login(@Valid @RequestBody AdminLoginRequestDTO request) {
        return ResponseEntity.ok(adminService.login(request));
    }
    
    @GetMapping("/{adminId}")
    @Operation(summary = "Get admin by ID")
    public ResponseEntity<AdminDTO> getAdmin(@PathVariable Long adminId) {
        return ResponseEntity.ok(adminService.getAdminById(adminId));
    }
}




