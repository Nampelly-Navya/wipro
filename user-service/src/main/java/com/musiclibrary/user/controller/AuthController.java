package com.musiclibrary.user.controller;

import com.musiclibrary.user.dto.LoginRequestDTO;
import com.musiclibrary.user.dto.LoginResponseDTO;
import com.musiclibrary.user.dto.UserDTO;
import com.musiclibrary.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and login APIs")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(authService.register(userDTO), HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
    
    @PostMapping("/logout")
    @Operation(summary = "User logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authService.logout(token));
    }
}
