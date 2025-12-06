package com.musiclibrary.user.controller;

import com.musiclibrary.user.dto.SongDTO;
import com.musiclibrary.user.dto.UserDTO;
import com.musiclibrary.user.entity.User;
import com.musiclibrary.user.repository.UserRepository;
import com.musiclibrary.user.service.FileStorageService;
import com.musiclibrary.user.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Songs", description = "Song viewing and searching APIs")
public class SongController {
    
    private final SongService songService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final FileStorageService fileStorageService;
    
    @PostMapping("/users/sync/song")
    @Operation(summary = "Sync song from Admin Service")
    public ResponseEntity<String> syncSong(@RequestBody SongDTO songDTO) {
        songService.syncSong(songDTO);
        
        // Copy audio file from Admin Service to User Service if audioUrl exists
        if (songDTO.getAudioUrl() != null && !songDTO.getAudioUrl().isEmpty()) {
            fileStorageService.copyAudioFromAdmin(songDTO.getAudioUrl());
        }
        
        return ResponseEntity.ok("Song synced: " + songDTO.getSongName());
    }
    
    @GetMapping("/users/all")
    @Operation(summary = "Get all registered users (for notification)")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(user -> {
                    UserDTO dto = modelMapper.map(user, UserDTO.class);
                    dto.setPassword(null); // Don't expose password
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }
    
    @GetMapping("/user/songs")
    @Operation(summary = "Get available songs")
    public ResponseEntity<List<SongDTO>> getAvailableSongs() {
        return ResponseEntity.ok(songService.getAvailableSongs());
    }
    
    @GetMapping("/user/songs/search")
    @Operation(summary = "Search songs")
    public ResponseEntity<List<SongDTO>> searchSongs(@RequestParam String query) {
        return ResponseEntity.ok(songService.searchSongs(query));
    }
    
    @GetMapping("/user/songs/{songId}")
    @Operation(summary = "Get song by ID")
    public ResponseEntity<SongDTO> getSongById(@PathVariable Long songId) {
        return ResponseEntity.ok(songService.getSongById(songId));
    }
    
    @DeleteMapping("/users/songs/{songId}")
    @Operation(summary = "Delete song (synced from Admin Service)")
    public ResponseEntity<String> deleteSong(@PathVariable Long songId) {
        songService.deleteSong(songId);
        return ResponseEntity.ok("Song deleted from User Service: " + songId);
    }

    @GetMapping("/user/songs/{songId}/stream")
    @Operation(summary = "Stream audio file for a song")
    public ResponseEntity<Resource> streamAudio(@PathVariable Long songId) {
        try {
            System.out.println("=== Streaming audio for song ID: " + songId + " ===");
            SongDTO song = songService.getSongById(songId);
            
            if (song.getAudioUrl() == null || song.getAudioUrl().isEmpty()) {
                System.out.println("ERROR: No audio URL for song " + songId);
                return ResponseEntity.notFound().build();
            }
            
            Path filePath = fileStorageService.getAudioFilePath(song.getAudioUrl());
            System.out.println("Audio file path: " + filePath.toAbsolutePath());
            
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                System.out.println("✅ File exists and is readable");
                String contentType = "audio/mpeg";
                String filename = song.getAudioUrl();
                if (filename.endsWith(".wav")) contentType = "audio/wav";
                else if (filename.endsWith(".ogg")) contentType = "audio/ogg";
                else if (filename.endsWith(".flac")) contentType = "audio/flac";
                else if (filename.endsWith(".m4a")) contentType = "audio/mp4";
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                System.out.println("❌ File does NOT exist at: " + filePath.toAbsolutePath());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("ERROR streaming audio: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/songs/{songId}/cover")
    @Operation(summary = "Get cover image for a song")
    public ResponseEntity<Resource> getCoverImage(@PathVariable Long songId) {
        try {
            SongDTO song = songService.getSongById(songId);
            
            if (song.getCoverImageUrl() == null || song.getCoverImageUrl().isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Path filePath = fileStorageService.getCoverImagePath(song.getCoverImageUrl());
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = "image/jpeg";
                String filename = song.getCoverImageUrl();
                if (filename.endsWith(".png")) contentType = "image/png";
                else if (filename.endsWith(".gif")) contentType = "image/gif";
                else if (filename.endsWith(".webp")) contentType = "image/webp";
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
