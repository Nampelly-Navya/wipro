package com.musiclibrary.user.feign;

import com.musiclibrary.user.dto.WelcomeEmailDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class NotificationServiceClientFallback implements NotificationServiceClient {
    
    @Override
    public ResponseEntity<Map<String, String>> sendWelcomeEmail(WelcomeEmailDTO dto) {
        return ResponseEntity.ok(Map.of("status", "queued", "message", "Welcome email queued - service unavailable"));
    }
}




