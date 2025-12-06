package com.musiclibrary.admin.controller;

import com.musiclibrary.admin.dto.SongDTO;
import com.musiclibrary.admin.entity.Song.SongStatus;
import com.musiclibrary.admin.service.FileStorageService;
import com.musiclibrary.admin.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/songs")
@RequiredArgsConstructor
@Tag(name = "Song Management", description = "APIs for managing songs")
public class SongController {
    
    private final SongService songService;
    private final FileStorageService fileStorageService;
    
    @PostMapping
    @Operation(summary = "Add new song")
    public ResponseEntity<SongDTO> addSong(@Valid @RequestBody SongDTO songDTO) {
        return new ResponseEntity<>(songService.addSong(songDTO), HttpStatus.CREATED);
    }
    
    @GetMapping
    @Operation(summary = "Get all songs")
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        return ResponseEntity.ok(songService.getAllSongs());
    }
    
    @GetMapping("/{songId}")
    @Operation(summary = "Get song by ID")
    public ResponseEntity<SongDTO> getSongById(@PathVariable Long songId) {
        return ResponseEntity.ok(songService.getSongById(songId));
    }
    
    @GetMapping("/available")
    @Operation(summary = "Get available songs")
    public ResponseEntity<List<SongDTO>> getAvailableSongs() {
        return ResponseEntity.ok(songService.getAvailableSongs());
    }
    
    @PutMapping("/{songId}")
    @Operation(summary = "Update song")
    public ResponseEntity<SongDTO> updateSong(@PathVariable Long songId, @Valid @RequestBody SongDTO songDTO) {
        return ResponseEntity.ok(songService.updateSong(songId, songDTO));
    }
    
    @DeleteMapping("/{songId}")
    @Operation(summary = "Delete song")
    public ResponseEntity<String> deleteSong(@PathVariable Long songId) {
        songService.deleteSong(songId);
        return ResponseEntity.ok("Song deleted successfully");
    }
    
    @PutMapping("/{songId}/status")
    @Operation(summary = "Update song status")
    public ResponseEntity<SongDTO> updateSongStatus(@PathVariable Long songId, @RequestParam SongStatus status) {
        return ResponseEntity.ok(songService.updateSongStatus(songId, status));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search songs")
    public ResponseEntity<List<SongDTO>> searchSongs(@RequestParam String query) {
        return ResponseEntity.ok(songService.searchSongs(query));
    }
    
    @PostMapping("/sync-all")
    @Operation(summary = "Sync all songs to User Service")
    public ResponseEntity<String> syncAllSongs() {
        int count = songService.syncAllSongsToUserService();
        return ResponseEntity.ok("Successfully synced " + count + " songs to User Service");
    }

    @PostMapping("/{songId}/upload-audio")
    @Operation(summary = "Upload audio file for a song")
    public ResponseEntity<?> uploadAudioFile(
            @PathVariable Long songId,
            @RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please select an audio file"));
        }
        
        if (!fileStorageService.isValidAudioFile(file)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid audio format. Supported: MP3, WAV, OGG, FLAC, AAC"));
        }
        
        String filename = fileStorageService.storeAudioFile(file);
        SongDTO updatedSong = songService.updateSongAudio(songId, filename);
        
        return ResponseEntity.ok(Map.of(
            "message", "Audio uploaded successfully",
            "audioUrl", filename,
            "song", updatedSong
        ));
    }

    @PostMapping("/{songId}/upload-cover")
    @Operation(summary = "Upload cover image for a song")
    public ResponseEntity<?> uploadCoverImage(
            @PathVariable Long songId,
            @RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please select an image file"));
        }
        
        if (!fileStorageService.isValidImageFile(file)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid image format. Supported: JPEG, PNG, GIF, WEBP"));
        }
        
        String filename = fileStorageService.storeCoverImage(file);
        SongDTO updatedSong = songService.updateSongCover(songId, filename);
        
        return ResponseEntity.ok(Map.of(
            "message", "Cover image uploaded successfully",
            "coverImageUrl", filename,
            "song", updatedSong
        ));
    }

    @GetMapping("/audio/{songId}")
    @Operation(summary = "Stream audio file by song ID")
    public ResponseEntity<Resource> streamAudioBySongId(@PathVariable Long songId) {
        try {
            System.out.println("=== Streaming audio for song ID: " + songId + " ===");
            SongDTO song = songService.getSongById(songId);
            String filename = song.getAudioUrl();
            
            System.out.println("Song: " + song.getSongName());
            System.out.println("Audio URL: " + filename);
            
            if (filename == null || filename.isEmpty()) {
                System.out.println("ERROR: No audio URL for song " + songId);
                return ResponseEntity.notFound().build();
            }
            
            Path filePath = fileStorageService.getAudioFilePath(filename);
            System.out.println("Audio file path: " + filePath.toAbsolutePath());
            
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                System.out.println("File exists and is readable. Size: " + resource.contentLength() + " bytes");
                String contentType = "audio/mpeg";
                if (filename.endsWith(".wav")) contentType = "audio/wav";
                else if (filename.endsWith(".ogg")) contentType = "audio/ogg";
                else if (filename.endsWith(".flac")) contentType = "audio/flac";
                else if (filename.endsWith(".m4a")) contentType = "audio/mp4";
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                        .body(resource);
            } else {
                System.out.println("ERROR: File does not exist or is not readable at: " + filePath.toAbsolutePath());
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("ERROR streaming audio for song " + songId + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cover/{songId}")
    @Operation(summary = "Get cover image by song ID")
    public ResponseEntity<Resource> getCoverImageBySongId(@PathVariable Long songId) {
        try {
            SongDTO song = songService.getSongById(songId);
            String filename = song.getCoverImageUrl();
            
            if (filename == null || filename.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Path filePath = fileStorageService.getCoverImagePath(filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                String contentType = "image/jpeg";
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

    @GetMapping("/audio/file/{filename}")
    @Operation(summary = "Stream audio file by filename")
    public ResponseEntity<Resource> streamAudioByFilename(@PathVariable String filename) {
        try {
            Path filePath = fileStorageService.getAudioFilePath(filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                String contentType = "audio/mpeg";
                if (filename.endsWith(".wav")) contentType = "audio/wav";
                else if (filename.endsWith(".ogg")) contentType = "audio/ogg";
                else if (filename.endsWith(".flac")) contentType = "audio/flac";
                
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cover/file/{filename}")
    @Operation(summary = "Get cover image by filename")
    public ResponseEntity<Resource> getCoverImageByFilename(@PathVariable String filename) {
        try {
            Path filePath = fileStorageService.getCoverImagePath(filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                String contentType = "image/jpeg";
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
