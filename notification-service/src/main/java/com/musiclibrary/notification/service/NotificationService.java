package com.musiclibrary.notification.service;

import com.musiclibrary.notification.dto.NotificationDTO;
import com.musiclibrary.notification.dto.NotificationResponseDTO;
import com.musiclibrary.notification.entity.Notification;
import com.musiclibrary.notification.entity.Notification.NotificationStatus;
import com.musiclibrary.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    public NotificationResponseDTO notifyNewSong(NotificationDTO dto) {
        log.info("===========================================");
        log.info("NEW SONG NOTIFICATION");
        log.info("Song: {}", dto.getSongName());
        log.info("Message: {}", dto.getMessage());
        if (dto.getEmail() != null) {
            log.info("Email: {}", dto.getEmail());
        }
        log.info("===========================================");
        
        // Print to console
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║          🎵 NEW SONG NOTIFICATION 🎵                     ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  Song: " + dto.getSongName());
        if (dto.getMessage() != null) {
            System.out.println("║  " + dto.getMessage());
        }
        if (dto.getEmail() != null) {
            System.out.println("║  Email: " + dto.getEmail());
        }
        System.out.println("║  ✅ NOTIFICATION RECORDED SUCCESSFULLY");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Save to database
        Notification notification = new Notification();
        notification.setSongName(dto.getSongName() != null ? dto.getSongName() : "Unknown");
        notification.setMessage(dto.getMessage() != null ? dto.getMessage() : "New song added");
        notification.setStatus(NotificationStatus.SENT);
        notification.setCreatedAt(LocalDateTime.now());
        
        Notification saved = notificationRepository.save(notification);
        return mapToResponse(saved);
    }
    
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private NotificationResponseDTO mapToResponse(Notification n) {
        return new NotificationResponseDTO(n.getId(), n.getSongName(), 
                n.getMessage(), n.getStatus().name(), n.getCreatedAt());
    }
}
