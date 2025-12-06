package com.musiclibrary.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    
    @Column(nullable = false)
    private String adminName;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String mobile;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminStatus adminStatus = AdminStatus.ACTIVE;
    
    public enum AdminStatus {
        ACTIVE, INACTIVE
    }
}
