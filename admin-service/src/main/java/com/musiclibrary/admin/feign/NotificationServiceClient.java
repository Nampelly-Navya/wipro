package com.musiclibrary.admin.feign;

import com.musiclibrary.admin.dto.LoginNotificationDTO;
import com.musiclibrary.admin.dto.NotificationDTO;
import com.musiclibrary.admin.dto.WelcomeEmailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "notification-service", fallback = NotificationServiceClientFallback.class)
public interface NotificationServiceClient {
    
    @PostMapping("/notify/new-song")
    ResponseEntity<Map<String, String>> notifyNewSong(@RequestBody NotificationDTO notification);
    
    @PostMapping("/notify/new-song/email")
    ResponseEntity<Map<String, String>> sendNewSongEmail(@RequestBody NotificationDTO notification);
    
    @PostMapping("/notify/welcome")
    ResponseEntity<Map<String, String>> sendWelcomeEmail(@RequestBody WelcomeEmailDTO dto);
    
    @PostMapping("/notify/login")
    ResponseEntity<Map<String, String>> sendLoginNotification(@RequestBody LoginNotificationDTO dto);
}
