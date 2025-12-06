package com.musiclibrary.notification.controller;

import com.musiclibrary.notification.dto.*;
import com.musiclibrary.notification.service.EmailService;
import com.musiclibrary.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification APIs")
public class NotificationController {
    
    private final NotificationService notificationService;
    private final EmailService emailService;
    
    @PostMapping("/new-song")
    @Operation(summary = "Notify new song added (console + DB)")
    public ResponseEntity<NotificationResponseDTO> notifyNewSong(@RequestBody NotificationDTO dto) {
        return ResponseEntity.ok(notificationService.notifyNewSong(dto));
    }
    
    @PostMapping("/new-song/email")
    @Operation(summary = "Send new song email notification to a user")
    public ResponseEntity<Map<String, String>> notifyNewSongByEmail(@RequestBody NotificationDTO dto) {
        emailService.sendNewSongEmailToUser(dto);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Email sent to " + dto.getEmail()));
    }
    
    @PostMapping("/new-song/email/bulk")
    @Operation(summary = "Send new song email notification to multiple users")
    public ResponseEntity<Map<String, String>> notifyNewSongByEmailBulk(@RequestBody SongNotificationEmailDTO dto) {
        emailService.sendNewSongNotification(dto);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Email notifications queued"));
    }
    
    @PostMapping("/welcome")
    @Operation(summary = "Send welcome email")
    public ResponseEntity<Map<String, String>> sendWelcomeEmail(@RequestBody WelcomeEmailDTO dto) {
        emailService.sendWelcomeEmail(dto);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Welcome email sent to " + dto.getToEmail()));
    }
    
    @PostMapping("/login")
    @Operation(summary = "Send login notification email")
    public ResponseEntity<Map<String, String>> sendLoginNotification(@RequestBody LoginNotificationDTO dto) {
        emailService.sendLoginNotification(dto);
        return ResponseEntity.ok(Map.of("status", "success", "message", "Login notification sent to " + dto.getToEmail()));
    }
    
    @GetMapping("/all")
    @Operation(summary = "Get all notifications")
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }
}
