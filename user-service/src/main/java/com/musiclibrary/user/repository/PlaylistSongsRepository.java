package com.musiclibrary.user.repository;

import com.musiclibrary.user.entity.PlaylistSongs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaylistSongsRepository extends JpaRepository<PlaylistSongs, Long> {
    List<PlaylistSongs> findByPlaylistId(Long playlistId);
    boolean existsByPlaylistIdAndSongId(Long playlistId, Long songId);
    void deleteByPlaylistIdAndSongId(Long playlistId, Long songId);
    void deleteByPlaylistId(Long playlistId);
}
