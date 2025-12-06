package com.musiclibrary.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResponseDTO {
    private String status;
    private String action;
    private SongDTO currentSong;
    private List<SongDTO> queue;
    private String message;
    
    public PlayerResponseDTO(String status, String action, String message) {
        this.status = status;
        this.action = action;
        this.message = message;
    }
    
    public PlayerResponseDTO(String status, String action, SongDTO currentSong, String message) {
        this.status = status;
        this.action = action;
        this.currentSong = currentSong;
        this.message = message;
    }
}
