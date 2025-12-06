package com.musiclibrary.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginRequestDTO {
    @NotBlank(message = "Username required")
    private String username;
    
    @NotBlank(message = "Password required")
    private String password;
}




