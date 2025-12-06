package com.musiclibrary.admin.service.impl;

import com.musiclibrary.admin.dto.NotificationDTO;
import com.musiclibrary.admin.dto.SongDTO;
import com.musiclibrary.admin.dto.UserDTO;
import com.musiclibrary.admin.entity.Song;
import com.musiclibrary.admin.entity.Song.SongStatus;
import com.musiclibrary.admin.exception.ResourceNotFoundException;
import com.musiclibrary.admin.feign.NotificationServiceClient;
import com.musiclibrary.admin.feign.UserServiceClient;
import com.musiclibrary.admin.repository.SongRepository;
import com.musiclibrary.admin.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SongServiceImpl implements SongService {
    
    private final SongRepository songRepository;
    private final ModelMapper modelMapper;
    private final UserServiceClient userServiceClient;
    private final NotificationServiceClient notificationServiceClient;
    
    @Override
    public SongDTO addSong(SongDTO songDTO) {
        log.info("═══════════════════════════════════════════════════════════");
        log.info("ADDING NEW SONG: {}", songDTO.getSongName());
        log.info("═══════════════════════════════════════════════════════════");
        
        // Step 1: Save song to Admin Service database
        log.info("Step 1: Saving song to database...");
        Song song = modelMapper.map(songDTO, Song.class);
        song = songRepository.save(song);
        SongDTO savedSong = modelMapper.map(song, SongDTO.class);
        log.info("✅ Song saved with ID: {}", savedSong.getSongId());
        
        // Step 2: Sync song to User Service
        try {
            log.info("Step 2: Syncing song to User Service...");
            ResponseEntity<String> syncResponse = userServiceClient.syncSong(savedSong);
            log.info("✅ Song synced to User Service: {}", syncResponse.getBody());
        } catch (Exception e) {
            log.warn("⚠️ Failed to sync song to User Service: {}", e.getMessage());
        }
        
        // Step 3: Fetch ALL users from User Service
        List<UserDTO> allUsers = null;
        try {
            log.info("Step 3: Fetching all users from User Service...");
            ResponseEntity<List<UserDTO>> usersResponse = userServiceClient.getAllUsers();
            allUsers = usersResponse.getBody();
            
            if (allUsers != null && !allUsers.isEmpty()) {
                log.info("✅ Fetched {} users from User Service", allUsers.size());
            } else {
                log.info("⚠️ No users found in User Service");
            }
        } catch (Exception e) {
            log.warn("⚠️ Failed to fetch users from User Service: {}", e.getMessage());
        }
        
        // Step 4: Send email notification to EVERY user
        if (allUsers != null && !allUsers.isEmpty()) {
            log.info("Step 4: Sending email notifications to {} users...", allUsers.size());
            int successCount = 0;
            int failCount = 0;
            
            for (UserDTO user : allUsers) {
                try {
                    // Create notification DTO for each user
                    NotificationDTO notification = new NotificationDTO();
                    notification.setEmail(user.getEmail());
                    notification.setSubject("🎵 New Song Added - Music Library");
                    notification.setSongName(savedSong.getSongName());
                    notification.setMessage("A new song '" + savedSong.getSongName() + "' by " 
                            + savedSong.getSinger() + " is available in the Music Library.");
                    
                    log.info("📧 Sending notification to: {} ({})", user.getFirstName(), user.getEmail());
                    
                    // Call Notification Service
                    notificationServiceClient.sendNewSongEmail(notification);
                    successCount++;
                    
                } catch (Exception e) {
                    failCount++;
                    log.warn("❌ Failed to send notification to {}: {}", user.getEmail(), e.getMessage());
                }
            }
            
            log.info("═══════════════════════════════════════════════════════════");
            log.info("NOTIFICATION SUMMARY");
            log.info("Total Users: {}", allUsers.size());
            log.info("✅ Sent Successfully: {}", successCount);
            log.info("❌ Failed: {}", failCount);
            log.info("═══════════════════════════════════════════════════════════");
        }
        
        // Step 5: Also save notification record (console style)
        try {
            NotificationDTO consoleNotification = new NotificationDTO();
            consoleNotification.setSongName(savedSong.getSongName());
            consoleNotification.setMessage("A new song '" + savedSong.getSongName() + "' has been added to the library.");
            notificationServiceClient.notifyNewSong(consoleNotification);
        } catch (Exception e) {
            log.warn("⚠️ Failed to save notification record: {}", e.getMessage());
        }
        
        log.info("═══════════════════════════════════════════════════════════");
        log.info("✅ SONG ADDED SUCCESSFULLY: {}", savedSong.getSongName());
        log.info("═══════════════════════════════════════════════════════════");
        
        return savedSong;
    }
    
    @Override
    @Transactional(readOnly = true)
    public SongDTO getSongById(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "songId", songId));
        return modelMapper.map(song, SongDTO.class);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SongDTO> getAllSongs() {
        return songRepository.findAll().stream()
                .map(song -> modelMapper.map(song, SongDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SongDTO> getAvailableSongs() {
        return songRepository.findBySongStatus(SongStatus.AVAILABLE).stream()
                .map(song -> modelMapper.map(song, SongDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public SongDTO updateSong(Long songId, SongDTO songDTO) {
        // NOTE: No notification sent on update - only on NEW song
        log.info("═══════════════════════════════════════════════════════════");
        log.info("UPDATING SONG ID: {}", songId);
        log.info("═══════════════════════════════════════════════════════════");
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "songId", songId));
        
        song.setSongName(songDTO.getSongName());
        song.setMusicDirector(songDTO.getMusicDirector());
        song.setSinger(songDTO.getSinger());
        song.setReleaseDate(songDTO.getReleaseDate());
        song.setAlbumName(songDTO.getAlbumName());
        song.setSongType(songDTO.getSongType());
        song.setSongStatus(songDTO.getSongStatus());
        
        Song savedSong = songRepository.save(song);
        SongDTO updatedSong = modelMapper.map(savedSong, SongDTO.class);
        log.info("✅ Song updated in Admin DB: {}", updatedSong.getSongName());
        
        // Sync updated song to User Service
        try {
            log.info("Syncing updated song to User Service...");
            ResponseEntity<String> syncResponse = userServiceClient.syncSong(updatedSong);
            log.info("✅ Updated song synced to User Service: {}", syncResponse.getBody());
        } catch (Exception e) {
            log.warn("⚠️ Failed to sync updated song to User Service: {}", e.getMessage());
        }
        
        return updatedSong;
    }
    
    @Override
    public void deleteSong(Long songId) {
        // NOTE: No notification sent on delete - only on NEW song
        log.info("═══════════════════════════════════════════════════════════");
        log.info("DELETING SONG ID: {}", songId);
        log.info("═══════════════════════════════════════════════════════════");
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "songId", songId));
        
        // Delete from User Service first
        try {
            log.info("Deleting song from User Service...");
            userServiceClient.deleteSong(songId);
            log.info("✅ Song deleted from User Service");
        } catch (Exception e) {
            log.warn("⚠️ Failed to delete song from User Service: {}", e.getMessage());
        }
        
        songRepository.delete(song);
        log.info("✅ Song deleted from Admin DB: {}", song.getSongName());
    }
    
    @Override
    public SongDTO updateSongStatus(Long songId, SongStatus status) {
        // NOTE: No notification sent on status change - only on NEW song
        log.info("═══════════════════════════════════════════════════════════");
        log.info("UPDATING STATUS for song ID: {} to {}", songId, status);
        log.info("═══════════════════════════════════════════════════════════");
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "songId", songId));
        song.setSongStatus(status);
        
        Song savedSong = songRepository.save(song);
        SongDTO updatedSong = modelMapper.map(savedSong, SongDTO.class);
        log.info("✅ Song status updated in Admin DB: {} -> {}", song.getSongName(), status);
        
        // Sync status change to User Service
        try {
            log.info("Syncing status change to User Service...");
            ResponseEntity<String> syncResponse = userServiceClient.syncSong(updatedSong);
            log.info("✅ Status change synced to User Service: {}", syncResponse.getBody());
        } catch (Exception e) {
            log.warn("⚠️ Failed to sync status change to User Service: {}", e.getMessage());
        }
        
        return updatedSong;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SongDTO> searchSongs(String query) {
        return songRepository.searchSongs(query).stream()
                .map(song -> modelMapper.map(song, SongDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public int syncAllSongsToUserService() {
        log.info("═══════════════════════════════════════════════════════════");
        log.info("SYNCING ALL SONGS TO USER SERVICE");
        log.info("═══════════════════════════════════════════════════════════");
        
        List<SongDTO> allSongs = songRepository.findAll().stream()
                .map(song -> modelMapper.map(song, SongDTO.class))
                .collect(Collectors.toList());
        
        int successCount = 0;
        int failCount = 0;
        
        for (SongDTO song : allSongs) {
            try {
                userServiceClient.syncSong(song);
                successCount++;
                log.info("✅ Synced: {} (ID: {})", song.getSongName(), song.getSongId());
            } catch (Exception e) {
                failCount++;
                log.error("❌ Failed to sync: {} - {}", song.getSongName(), e.getMessage());
            }
        }
        
        log.info("═══════════════════════════════════════════════════════════");
        log.info("SYNC COMPLETE - Success: {}, Failed: {}", successCount, failCount);
        log.info("═══════════════════════════════════════════════════════════");
        
        return successCount;
    }

    @Override
    public SongDTO updateSongAudio(Long songId, String audioFilename) {
        log.info("Updating audio for song ID: {} with file: {}", songId, audioFilename);
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "songId", songId));
        
        song.setAudioUrl(audioFilename);
        Song savedSong = songRepository.save(song);
        SongDTO updatedSong = modelMapper.map(savedSong, SongDTO.class);
        
        // Sync to User Service
        try {
            userServiceClient.syncSong(updatedSong);
            log.info("✅ Audio update synced to User Service");
        } catch (Exception e) {
            log.warn("⚠️ Failed to sync audio update to User Service: {}", e.getMessage());
        }
        
        return updatedSong;
    }

    @Override
    public SongDTO updateSongCover(Long songId, String coverFilename) {
        log.info("Updating cover for song ID: {} with file: {}", songId, coverFilename);
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "songId", songId));
        
        song.setCoverImageUrl(coverFilename);
        Song savedSong = songRepository.save(song);
        SongDTO updatedSong = modelMapper.map(savedSong, SongDTO.class);
        
        // Sync to User Service
        try {
            userServiceClient.syncSong(updatedSong);
            log.info("✅ Cover update synced to User Service");
        } catch (Exception e) {
            log.warn("⚠️ Failed to sync cover update to User Service: {}", e.getMessage());
        }
        
        return updatedSong;
    }
}
