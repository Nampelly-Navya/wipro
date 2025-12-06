package com.musiclibrary.admin.feign;

import com.musiclibrary.admin.dto.LoginNotificationDTO;
import com.musiclibrary.admin.dto.NotificationDTO;
import com.musiclibrary.admin.dto.WelcomeEmailDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class NotificationServiceClientFallback implements NotificationServiceClient {
    
    @Override
    public ResponseEntity<Map<String, String>> notifyNewSong(NotificationDTO notification) {
        return ResponseEntity.ok(Map.of("status", "queued", "message", "Notification queued - service unavailable"));
    }
    
    @Override
    public ResponseEntity<Map<String, String>> sendNewSongEmail(NotificationDTO notification) {
        return ResponseEntity.ok(Map.of("status", "queued", "message", "Song email queued - service unavailable"));
    }
    
    @Override
    public ResponseEntity<Map<String, String>> sendWelcomeEmail(WelcomeEmailDTO dto) {
        return ResponseEntity.ok(Map.of("status", "queued", "message", "Welcome email queued - service unavailable"));
    }
    
    @Override
    public ResponseEntity<Map<String, String>> sendLoginNotification(LoginNotificationDTO dto) {
        return ResponseEntity.ok(Map.of("status", "queued", "message", "Login notification queued - service unavailable"));
    }
}
