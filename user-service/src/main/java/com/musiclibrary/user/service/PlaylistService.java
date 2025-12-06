package com.musiclibrary.user.service;

import com.musiclibrary.user.dto.PlaylistDTO;
import com.musiclibrary.user.dto.SongDTO;
import com.musiclibrary.user.entity.Playlist;
import com.musiclibrary.user.entity.PlaylistSongs;
import com.musiclibrary.user.entity.Song;
import com.musiclibrary.user.entity.Song.SongStatus;
import com.musiclibrary.user.exception.DuplicateResourceException;
import com.musiclibrary.user.exception.ResourceNotFoundException;
import com.musiclibrary.user.exception.UnauthorizedException;
import com.musiclibrary.user.repository.PlaylistRepository;
import com.musiclibrary.user.repository.PlaylistSongsRepository;
import com.musiclibrary.user.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PlaylistService {
    
    private final PlaylistRepository playlistRepository;
    private final PlaylistSongsRepository playlistSongsRepository;
    private final SongRepository songRepository;
    private final ModelMapper modelMapper;
    
    public PlaylistDTO createPlaylist(PlaylistDTO dto, Long userId) {
        log.info("Creating playlist: {} for user: {}", dto.getPlaylistName(), userId);
        
        if (playlistRepository.existsByPlaylistNameAndUserId(dto.getPlaylistName(), userId)) {
            throw new DuplicateResourceException("Playlist already exists");
        }
        
        Playlist playlist = new Playlist();
        playlist.setPlaylistName(dto.getPlaylistName());
        playlist.setUserId(userId);
        playlist.setCreatedAt(LocalDateTime.now());
        playlist.setUpdatedAt(LocalDateTime.now());
        
        return modelMapper.map(playlistRepository.save(playlist), PlaylistDTO.class);
    }
    
    @Transactional(readOnly = true)
    public List<PlaylistDTO> getUserPlaylists(Long userId) {
        return playlistRepository.findByUserId(userId).stream()
                .map(p -> modelMapper.map(p, PlaylistDTO.class))
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PlaylistDTO getPlaylistById(Long playlistId, Long userId) {
        Playlist playlist = findAndValidate(playlistId, userId);
        return modelMapper.map(playlist, PlaylistDTO.class);
    }
    
    public PlaylistDTO updatePlaylist(Long playlistId, PlaylistDTO dto, Long userId) {
        Playlist playlist = findAndValidate(playlistId, userId);
        playlist.setPlaylistName(dto.getPlaylistName());
        playlist.setUpdatedAt(LocalDateTime.now());
        return modelMapper.map(playlistRepository.save(playlist), PlaylistDTO.class);
    }
    
    public void deletePlaylist(Long playlistId, Long userId) {
        Playlist playlist = findAndValidate(playlistId, userId);
        playlistSongsRepository.deleteByPlaylistId(playlistId);
        playlistRepository.delete(playlist);
    }
    
    public void addSongToPlaylist(Long playlistId, Long songId, Long userId) {
        findAndValidate(playlistId, userId);
        
        // Check if song exists and is available
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song", "songId", songId));
        
        // Don't allow adding unavailable songs to playlists
        if (song.getSongStatus() == SongStatus.NOTAVAILABLE) {
            throw new ResourceNotFoundException("Song", "songId", songId);
        }
        
        if (playlistSongsRepository.existsByPlaylistIdAndSongId(playlistId, songId)) {
            throw new DuplicateResourceException("Song already in playlist");
        }
        
        PlaylistSongs ps = new PlaylistSongs();
        ps.setPlaylistId(playlistId);
        ps.setSongId(songId);
        ps.setAddedAt(LocalDateTime.now());
        playlistSongsRepository.save(ps);
    }
    
    @Transactional(readOnly = true)
    public List<SongDTO> getPlaylistSongs(Long playlistId, Long userId) {
        findAndValidate(playlistId, userId);
        
        List<Long> songIds = playlistSongsRepository.findByPlaylistId(playlistId).stream()
                .map(PlaylistSongs::getSongId)
                .collect(Collectors.toList());
        
        // Only return AVAILABLE songs - filter out unavailable songs
        return songRepository.findBySongIdIn(songIds).stream()
                .filter(s -> s.getSongStatus() == SongStatus.AVAILABLE)
                .map(s -> {
                    SongDTO dto = new SongDTO();
                    dto.setSongId(s.getSongId());
                    dto.setSongName(s.getSongName());
                    dto.setMusicDirector(s.getMusicDirector());
                    dto.setSinger(s.getSinger());
                    dto.setReleaseDate(s.getReleaseDate());
                    dto.setAlbumName(s.getAlbumName());
                    dto.setSongType(s.getSongType().name());
                    dto.setSongStatus(s.getSongStatus().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    public void removeSongFromPlaylist(Long playlistId, Long songId, Long userId) {
        findAndValidate(playlistId, userId);
        
        if (!playlistSongsRepository.existsByPlaylistIdAndSongId(playlistId, songId)) {
            throw new ResourceNotFoundException("Song not in playlist");
        }
        
        playlistSongsRepository.deleteByPlaylistIdAndSongId(playlistId, songId);
    }
    
    @Transactional(readOnly = true)
    public List<SongDTO> searchPlaylistSongs(Long playlistId, String query, Long userId) {
        findAndValidate(playlistId, userId);
        
        List<Long> songIds = playlistSongsRepository.findByPlaylistId(playlistId).stream()
                .map(PlaylistSongs::getSongId)
                .collect(Collectors.toList());
        
        String q = query.toLowerCase();
        // Only return AVAILABLE songs - filter out unavailable songs
        return songRepository.findBySongIdIn(songIds).stream()
                .filter(s -> s.getSongStatus() == SongStatus.AVAILABLE)
                .filter(s -> s.getSongName().toLowerCase().contains(q) ||
                            (s.getAlbumName() != null && s.getAlbumName().toLowerCase().contains(q)) ||
                            s.getSinger().toLowerCase().contains(q) ||
                            s.getMusicDirector().toLowerCase().contains(q))
                .map(s -> {
                    SongDTO dto = new SongDTO();
                    dto.setSongId(s.getSongId());
                    dto.setSongName(s.getSongName());
                    dto.setMusicDirector(s.getMusicDirector());
                    dto.setSinger(s.getSinger());
                    dto.setReleaseDate(s.getReleaseDate());
                    dto.setAlbumName(s.getAlbumName());
                    dto.setSongType(s.getSongType().name());
                    dto.setSongStatus(s.getSongStatus().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    private Playlist findAndValidate(Long playlistId, Long userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", "playlistId", playlistId));
        
        if (!playlist.getUserId().equals(userId)) {
            throw new UnauthorizedException("Access denied to this playlist");
        }
        return playlist;
    }
}
