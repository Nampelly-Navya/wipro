package com.musiclibrary.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String email;
    private String subject;
    private String songName;
    private String message;
    
    // Constructor for new song notification
    public NotificationDTO(String email, String songName) {
        this.email = email;
        this.subject = "🎵 New Song Added - Music Library";
        this.songName = songName;
        this.message = "A new song '" + songName + "' is available in the Music Library.";
    }
}
