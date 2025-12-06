package com.musiclibrary.user.repository;

import com.musiclibrary.user.entity.Song;
import com.musiclibrary.user.entity.Song.SongStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findBySongStatus(SongStatus status);
    
    @Query("SELECT s FROM Song s WHERE s.songStatus = 'AVAILABLE' AND (" +
           "LOWER(s.songName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(s.albumName) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(s.musicDirector) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(s.singer) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Song> searchAvailableSongs(@Param("q") String query);
    
    List<Song> findBySongIdIn(List<Long> songIds);
}
