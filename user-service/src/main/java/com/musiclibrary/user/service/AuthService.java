package com.musiclibrary.user.service;

import com.musiclibrary.user.dto.LoginRequestDTO;
import com.musiclibrary.user.dto.LoginResponseDTO;
import com.musiclibrary.user.dto.UserDTO;
import com.musiclibrary.user.dto.WelcomeEmailDTO;
import com.musiclibrary.user.entity.User;
import com.musiclibrary.user.exception.AuthenticationException;
import com.musiclibrary.user.exception.DuplicateResourceException;
import com.musiclibrary.user.feign.NotificationServiceClient;
import com.musiclibrary.user.repository.UserRepository;
import com.musiclibrary.user.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;
    private final NotificationServiceClient notificationServiceClient;
    
    public UserDTO register(UserDTO userDTO) {
        log.info("Registering user: {}", userDTO.getUsername());
        
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        
        User saved = userRepository.save(user);
        
        // Send welcome email
        try {
            log.info("Sending welcome email to user: {}", userDTO.getEmail());
            WelcomeEmailDTO welcomeEmail = new WelcomeEmailDTO();
            welcomeEmail.setToEmail(userDTO.getEmail());
            welcomeEmail.setName(userDTO.getFirstName() + " " + userDTO.getLastName());
            welcomeEmail.setUsername(userDTO.getUsername());
            welcomeEmail.setUserType("USER");
            notificationServiceClient.sendWelcomeEmail(welcomeEmail);
            log.info("Welcome email sent successfully");
        } catch (Exception e) {
            log.warn("Failed to send welcome email: {}", e.getMessage());
        }
        
        UserDTO response = modelMapper.map(saved, UserDTO.class);
        response.setPassword(null);
        return response;
    }
    
    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Login attempt: {}", request.getUsername());
        
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new AuthenticationException("User not found"));
            
            String token = jwtTokenProvider.generateToken(user.getUsername(), user.getUserId());
            return new LoginResponseDTO(token, user.getUserId(), user.getUsername());
            
        } catch (Exception e) {
            throw new AuthenticationException("Invalid username or password");
        }
    }
    
    public String logout(String token) {
        log.info("User logged out");
        return "Logged out successfully";
    }
}
