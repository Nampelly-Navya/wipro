package com.musiclibrary.admin.dto;

import com.musiclibrary.admin.entity.Song.SongStatus;
import com.musiclibrary.admin.entity.Song.SongType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDTO {
    private Long songId;
    
    @NotBlank(message = "Song name is required")
    private String songName;
    
    @NotBlank(message = "Music director is required")
    private String musicDirector;
    
    @NotBlank(message = "Singer is required")
    private String singer;
    
    private LocalDate releaseDate;
    private String albumName;
    private String audioUrl;
    private String coverImageUrl;
    private Integer durationSeconds;
    private SongType songType = SongType.FREE;
    private SongStatus songStatus = SongStatus.AVAILABLE;
}
