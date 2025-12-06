package com.musiclibrary.admin.service;

import com.musiclibrary.admin.dto.SongDTO;
import com.musiclibrary.admin.entity.Song.SongStatus;
import java.util.List;

public interface SongService {
    SongDTO addSong(SongDTO songDTO);
    SongDTO getSongById(Long songId);
    List<SongDTO> getAllSongs();
    List<SongDTO> getAvailableSongs();
    SongDTO updateSong(Long songId, SongDTO songDTO);
    void deleteSong(Long songId);
    SongDTO updateSongStatus(Long songId, SongStatus status);
    List<SongDTO> searchSongs(String query);
    int syncAllSongsToUserService();
    SongDTO updateSongAudio(Long songId, String audioFilename);
    SongDTO updateSongCover(Long songId, String coverFilename);
}
