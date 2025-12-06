package com.musiclibrary.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String mobile;
    private String address1;
    private String address2;
    private String city;
}




