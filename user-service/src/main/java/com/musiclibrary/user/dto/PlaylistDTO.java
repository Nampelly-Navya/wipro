package com.musiclibrary.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO {
    private Long playlistId;
    
    @NotBlank(message = "Playlist name required")
    private String playlistName;
    
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
