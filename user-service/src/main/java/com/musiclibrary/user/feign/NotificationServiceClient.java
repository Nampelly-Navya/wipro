package com.musiclibrary.user.feign;

import com.musiclibrary.user.dto.WelcomeEmailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "notification-service", fallback = NotificationServiceClientFallback.class)
public interface NotificationServiceClient {
    
    @PostMapping("/notify/welcome")
    ResponseEntity<Map<String, String>> sendWelcomeEmail(@RequestBody WelcomeEmailDTO dto);
}




