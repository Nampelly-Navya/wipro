package com.musiclibrary.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "songs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songId;
    
    @Column(nullable = false)
    private String songName;
    
    @Column(nullable = false)
    private String musicDirector;
    
    @Column(nullable = false)
    private String singer;
    
    private LocalDate releaseDate;
    
    private String albumName;
    
    private String audioUrl;  // URL/path to the audio file
    
    private String coverImageUrl;  // Album cover image
    
    private Integer durationSeconds;  // Song duration in seconds
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SongType songType = SongType.FREE;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SongStatus songStatus = SongStatus.AVAILABLE;
    
    public enum SongType {
        FREE, PREMIUM
    }
    
    public enum SongStatus {
        AVAILABLE, NOTAVAILABLE
    }
}
