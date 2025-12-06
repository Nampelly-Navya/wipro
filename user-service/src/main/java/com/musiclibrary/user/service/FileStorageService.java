package com.musiclibrary.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.audio-dir:audio}")
    private String audioDir;

    @Value("${file.image-dir:images}")
    private String imageDir;

    private Path audioStoragePath;
    private Path imageStoragePath;

    @PostConstruct
    public void init() {
        try {
            audioStoragePath = Paths.get(uploadDir, audioDir).toAbsolutePath().normalize();
            imageStoragePath = Paths.get(uploadDir, imageDir).toAbsolutePath().normalize();
            
            Files.createDirectories(audioStoragePath);
            Files.createDirectories(imageStoragePath);
            
            log.info("Audio storage path: {}", audioStoragePath);
            log.info("Image storage path: {}", imageStoragePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directories", e);
        }
    }

    public Path getAudioFilePath(String filename) {
        return audioStoragePath.resolve(filename).normalize();
    }

    public Path getCoverImagePath(String filename) {
        return imageStoragePath.resolve(filename).normalize();
    }

    public boolean audioFileExists(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }
        Path filePath = audioStoragePath.resolve(filename).normalize();
        return Files.exists(filePath);
    }

    public boolean coverImageExists(String filename) {
        if (filename == null || filename.isEmpty()) {
            return false;
        }
        Path filePath = imageStoragePath.resolve(filename).normalize();
        return Files.exists(filePath);
    }

    /**
     * Copy audio file from Admin Service storage to User Service storage
     */
    public void copyAudioFromAdmin(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            log.warn("Cannot copy audio: fileName is null or empty");
            return;
        }
        
        try {
            // Source: Admin Service audio directory
            Path sourcePath = Paths.get("C:/uploads/admin/audio", fileName);
            // Destination: User Service audio directory
            Path destPath = Paths.get("C:/uploads/user/audio", fileName);
            
            log.info("Copying audio file from Admin to User Service:");
            log.info("  Source: {}", sourcePath.toAbsolutePath());
            log.info("  Destination: {}", destPath.toAbsolutePath());
            
            // Create destination directory if it doesn't exist
            Files.createDirectories(destPath.getParent());
            
            // Check if source file exists
            if (Files.exists(sourcePath)) {
                Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("✅ Audio file copied successfully: {}", fileName);
            } else {
                log.warn("⚠️ Source audio file not found: {}", sourcePath.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("❌ Failed to copy audio file: {} - {}", fileName, e.getMessage());
        }
    }
}

