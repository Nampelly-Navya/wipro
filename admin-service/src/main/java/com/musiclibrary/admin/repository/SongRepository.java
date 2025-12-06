package com.musiclibrary.admin.repository;

import com.musiclibrary.admin.entity.Song;
import com.musiclibrary.admin.entity.Song.SongStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    
    List<Song> findBySongStatus(SongStatus songStatus);
    
    @Query("SELECT s FROM Song s WHERE " +
           "LOWER(s.songName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.albumName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.musicDirector) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(s.singer) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Song> searchSongs(@Param("query") String query);
}
