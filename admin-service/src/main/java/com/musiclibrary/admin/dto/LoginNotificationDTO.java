package com.musiclibrary.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginNotificationDTO {
    private String toEmail;
    private String name;
    private String username;
    private String userType;
}




