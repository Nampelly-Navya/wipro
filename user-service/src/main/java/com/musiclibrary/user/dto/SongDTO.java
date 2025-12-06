package com.musiclibrary.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDTO {
    private Long songId;
    private String songName;
    private String musicDirector;
    private String singer;
    private LocalDate releaseDate;
    private String albumName;
    private String audioUrl;
    private String coverImageUrl;
    private Integer durationSeconds;
    private String songType;    // Changed to String for cross-service compatibility
    private String songStatus;  // Changed to String for cross-service compatibility
}
