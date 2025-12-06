package com.musiclibrary.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginResponseDTO {
    private Long adminId;
    private String adminName;
    private String username;
    private String message;
    
    public AdminLoginResponseDTO(Long adminId, String adminName, String username) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.username = username;
        this.message = "Login successful";
    }
}




