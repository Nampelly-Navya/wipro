package com.musiclibrary.user.service;

import com.musiclibrary.user.dto.SongDTO;
import com.musiclibrary.user.entity.Song;
import com.musiclibrary.user.entity.Song.SongStatus;
import com.musiclibrary.user.entity.Song.SongType;
import com.musiclibrary.user.exception.ResourceNotFoundException;
import com.musiclibrary.user.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SongService {
    
    private final SongRepository songRepository;
    private final ModelMapper modelMapper;
    
    public SongDTO syncSong(SongDTO songDTO) {
        log.info("═══════════════════════════════════════════════════════════");
        log.info("SYNCING SONG FROM ADMIN SERVICE");
        log.info("Song ID: {}", songDTO.getSongId());
        log.info("Song Name: {}", songDTO.getSongName());
        log.info("Song Type: {}", songDTO.getSongType());
        log.info("Song Status: {}", songDTO.getSongStatus());
        log.info("═══════════════════════════════════════════════════════════");
        
        Song song = new Song();
        song.setSongId(songDTO.getSongId());
        song.setSongName(songDTO.getSongName());
        song.setMusicDirector(songDTO.getMusicDirector());
        song.setSinger(songDTO.getSinger());
        song.setReleaseDate(songDTO.getReleaseDate());
        song.setAlbumName(songDTO.getAlbumName());
        song.setAudioUrl(songDTO.getAudioUrl());
        song.setCoverImageUrl(songDTO.getCoverImageUrl());
        song.setDurationSeconds(songDTO.getDurationSeconds());
        
        // Convert String to Enum safely
        try {
            if (songDTO.getSongType() != null) {
                song.setSongType(SongType.valueOf(songDTO.getSongType().toString()));
            } else {
                song.setSongType(SongType.FREE);
            }
        } catch (Exception e) {
            log.warn("Invalid songType, defaulting to FREE");
            song.setSongType(SongType.FREE);
        }
        
        try {
            if (songDTO.getSongStatus() != null) {
                song.setSongStatus(SongStatus.valueOf(songDTO.getSongStatus().toString()));
            } else {
                song.setSongStatus(SongStatus.AVAILABLE);
            }
        } catch (Exception e) {
            log.warn("Invalid songStatus, defaulting to AVAILABLE");
            song.setSongStatus(SongStatus.AVAILABLE);
        }
        
        // Check if song already exists
        if (songDTO.getSongId() != null && songRepository.existsById(songDTO.getSongId())) {
            log.info("Song exists, updating...");
            Song existing = songRepository.findById(songDTO.getSongId()).get();
            existing.setSongName(song.getSongName());
            existing.setMusicDirector(song.getMusicDirector());
            existing.setSinger(song.getSinger());
            existing.setReleaseDate(song.getReleaseDate());
            existing.setAlbumName(song.getAlbumName());
            existing.setAudioUrl(song.getAudioUrl());
            existing.setCoverImageUrl(song.getCoverImageUrl());
            existing.setDurationSeconds(song.getDurationSeconds());
            existing.setSongType(song.getSongType());
            existing.setSongStatus(song.getSongStatus());
            song = songRepository.save(existing);
        } else {
            log.info("New song, saving...");
            song = songRepository.save(song);
        }
        
        log.info("✅ Song synced successfully with ID: {}", song.getSongId());
        
        // Convert back to DTO
        SongDTO result = new SongDTO();
        result.setSongId(song.getSongId());
        result.setSongName(song.getSongName());
        result.setMusicDirector(song.getMusicDirector());
        result.setSinger(song.getSinger());
        result.setReleaseDate(song.getReleaseDate());
        result.setAlbumName(song.getAlbumName());
        result.setAudioUrl(song.getAudioUrl());
        result.setCoverImageUrl(song.getCoverImageUrl());
        result.setDurationSeconds(song.getDurationSeconds());
        result.setSongType(song.getSongType().name());
        result.setSongStatus(song.getSongStatus().name());
        
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<SongDTO> getAvailableSongs() {
        log.info("Fetching AVAILABLE songs for user portal...");
        // Only show songs with AVAILABLE status - hide NOTAVAILABLE songs from users
        List<Song> songs = songRepository.findBySongStatus(SongStatus.AVAILABLE);
        log.info("Found {} available songs", songs.size());
        
        return songs.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SongDTO> getAllSongs() {
        log.info("Fetching ALL songs...");
        List<Song> songs = songRepository.findAll();
        log.info("Found {} total songs", songs.size());
        
        return songs.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SongDTO> searchSongs(String query) {
        return songRepository.searchAvailableSongs(query).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public SongDTO getSongById(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "songId", songId));
        
        // Check if song is available - users cannot access unavailable songs
        if (song.getSongStatus() == SongStatus.NOTAVAILABLE) {
            throw new ResourceNotFoundException("Song", "songId", songId);
        }
        
        return mapToDTO(song);
    }
    
    private SongDTO mapToDTO(Song song) {
        SongDTO dto = new SongDTO();
        dto.setSongId(song.getSongId());
        dto.setSongName(song.getSongName());
        dto.setMusicDirector(song.getMusicDirector());
        dto.setSinger(song.getSinger());
        dto.setReleaseDate(song.getReleaseDate());
        dto.setAlbumName(song.getAlbumName());
        dto.setAudioUrl(song.getAudioUrl());
        dto.setCoverImageUrl(song.getCoverImageUrl());
        dto.setDurationSeconds(song.getDurationSeconds());
        dto.setSongType(song.getSongType().name());
        dto.setSongStatus(song.getSongStatus().name());
        return dto;
    }
    
    public void deleteSong(Long songId) {
        log.info("═══════════════════════════════════════════════════════════");
        log.info("DELETING SONG FROM USER SERVICE: {}", songId);
        log.info("═══════════════════════════════════════════════════════════");
        
        if (songRepository.existsById(songId)) {
            songRepository.deleteById(songId);
            log.info("✅ Song deleted from User Service: {}", songId);
        } else {
            log.warn("⚠️ Song not found in User Service: {}", songId);
        }
    }
}
