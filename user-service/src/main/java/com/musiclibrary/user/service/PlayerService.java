package com.musiclibrary.user.service;

import com.musiclibrary.user.dto.PlayerResponseDTO;
import com.musiclibrary.user.dto.SongDTO;
import com.musiclibrary.user.entity.PlaylistSongs;
import com.musiclibrary.user.entity.Song;
import com.musiclibrary.user.exception.ResourceNotFoundException;
import com.musiclibrary.user.repository.PlaylistRepository;
import com.musiclibrary.user.repository.PlaylistSongsRepository;
import com.musiclibrary.user.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PlayerService {
    
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistSongsRepository playlistSongsRepository;
    
    public PlayerResponseDTO play(Long songId) {
        log.info("Playing song: {}", songId);
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "songId", songId));
        
        SongDTO songDTO = mapToDTO(song);
        String message = String.format("Now playing: '%s' by %s", song.getSongName(), song.getSinger());
        
        log.info(message);
        return new PlayerResponseDTO("PLAYING", "play", songDTO, message);
    }
    
    public PlayerResponseDTO stop() {
        log.info("Stopping playback");
        return new PlayerResponseDTO("STOPPED", "stop", "Playback stopped");
    }
    
    public PlayerResponseDTO shuffle(Long playlistId, Long userId) {
        log.info("Shuffling playlist: {}", playlistId);
        
        if (!playlistRepository.existsById(playlistId)) {
            throw new ResourceNotFoundException("Playlist", "playlistId", playlistId);
        }
        
        List<Long> songIds = playlistSongsRepository.findByPlaylistId(playlistId).stream()
                .map(PlaylistSongs::getSongId)
                .collect(Collectors.toList());
        
        if (songIds.isEmpty()) {
            return new PlayerResponseDTO("EMPTY", "shuffle", "Playlist is empty");
        }
        
        List<Song> songs = songRepository.findBySongIdIn(songIds);
        Collections.shuffle(songs);
        
        List<SongDTO> queue = songs.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        
        SongDTO first = queue.get(0);
        String message = String.format("Shuffle ON. Playing: '%s' by %s", first.getSongName(), first.getSinger());
        
        PlayerResponseDTO response = new PlayerResponseDTO();
        response.setStatus("SHUFFLE");
        response.setAction("shuffle");
        response.setCurrentSong(first);
        response.setQueue(queue);
        response.setMessage(message);
        
        return response;
    }
    
    public PlayerResponseDTO repeat(Long songId) {
        log.info("Repeat mode for song: {}", songId);
        
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "songId", songId));
        
        SongDTO songDTO = mapToDTO(song);
        String message = String.format("Repeat ON for: '%s' by %s", song.getSongName(), song.getSinger());
        
        return new PlayerResponseDTO("REPEAT", "repeat", songDTO, message);
    }
    
    private SongDTO mapToDTO(Song song) {
        SongDTO dto = new SongDTO();
        dto.setSongId(song.getSongId());
        dto.setSongName(song.getSongName());
        dto.setMusicDirector(song.getMusicDirector());
        dto.setSinger(song.getSinger());
        dto.setReleaseDate(song.getReleaseDate());
        dto.setAlbumName(song.getAlbumName());
        dto.setSongType(song.getSongType().name());
        dto.setSongStatus(song.getSongStatus().name());
        return dto;
    }
}
