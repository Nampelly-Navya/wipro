package com.musiclibrary.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    
    @NotBlank(message = "First name required")
    private String firstName;
    
    @NotBlank(message = "Last name required")
    private String lastName;
    
    @NotBlank(message = "Username required")
    private String username;
    
    @NotBlank(message = "Password required")
    private String password;
    
    @Email(message = "Invalid email")
    @NotBlank(message = "Email required")
    private String email;
    
    @NotBlank(message = "Mobile required")
    private String mobile;
    
    private String address1;
    private String address2;
    private String city;
}
