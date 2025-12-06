package com.musiclibrary.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WelcomeEmailDTO {
    private String toEmail;
    private String name;
    private String username;
    private String userType; // ADMIN or USER
}




