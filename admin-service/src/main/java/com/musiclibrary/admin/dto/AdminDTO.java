package com.musiclibrary.admin.dto;

import com.musiclibrary.admin.entity.Admin.AdminStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
    private Long adminId;
    
    @NotBlank(message = "Admin name is required")
    private String adminName;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Mobile is required")
    private String mobile;
    
    private AdminStatus adminStatus = AdminStatus.ACTIVE;
}
