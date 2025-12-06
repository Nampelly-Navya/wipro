package com.musiclibrary.user.repository;

import com.musiclibrary.user.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserId(Long userId);
    boolean existsByPlaylistNameAndUserId(String name, Long userId);
}
