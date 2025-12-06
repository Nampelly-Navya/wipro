package com.musiclibrary.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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

    public String storeAudioFile(MultipartFile file) {
        return storeFile(file, audioStoragePath, "audio");
    }

    public String storeCoverImage(MultipartFile file) {
        return storeFile(file, imageStoragePath, "image");
    }

    private String storeFile(MultipartFile file, Path storagePath, String type) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        
        // Validate file
        if (originalFilename.contains("..")) {
            throw new RuntimeException("Invalid file path: " + originalFilename);
        }

        // Generate unique filename
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        try {
            Path targetLocation = storagePath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("Stored {} file: {} -> {}", type, originalFilename, uniqueFilename);
            return uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store " + type + " file: " + originalFilename, e);
        }
    }

    public byte[] loadAudioFile(String filename) {
        return loadFile(audioStoragePath, filename);
    }

    public byte[] loadCoverImage(String filename) {
        return loadFile(imageStoragePath, filename);
    }

    private byte[] loadFile(Path storagePath, String filename) {
        try {
            Path filePath = storagePath.resolve(filename).normalize();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("File not found: " + filename, e);
        }
    }

    public void deleteAudioFile(String filename) {
        deleteFile(audioStoragePath, filename);
    }

    public void deleteCoverImage(String filename) {
        deleteFile(imageStoragePath, filename);
    }

    private void deleteFile(Path storagePath, String filename) {
        try {
            if (filename != null && !filename.isEmpty()) {
                Path filePath = storagePath.resolve(filename).normalize();
                Files.deleteIfExists(filePath);
                log.info("Deleted file: {}", filename);
            }
        } catch (IOException e) {
            log.warn("Failed to delete file: {}", filename, e);
        }
    }

    public Path getAudioFilePath(String filename) {
        return audioStoragePath.resolve(filename).normalize();
    }

    public Path getCoverImagePath(String filename) {
        return imageStoragePath.resolve(filename).normalize();
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex);
        }
        return "";
    }

    public boolean isValidAudioFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
            contentType.equals("audio/mpeg") ||      // MP3
            contentType.equals("audio/mp3") ||
            contentType.equals("audio/wav") ||       // WAV
            contentType.equals("audio/x-wav") ||
            contentType.equals("audio/ogg") ||       // OGG
            contentType.equals("audio/flac") ||      // FLAC
            contentType.equals("audio/aac") ||       // AAC
            contentType.equals("audio/mp4")          // M4A
        );
    }

    public boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
            contentType.equals("image/jpeg") ||
            contentType.equals("image/png") ||
            contentType.equals("image/gif") ||
            contentType.equals("image/webp")
        );
    }
}

