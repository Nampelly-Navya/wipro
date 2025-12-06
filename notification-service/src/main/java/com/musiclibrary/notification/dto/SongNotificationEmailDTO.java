package com.musiclibrary.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongNotificationEmailDTO {
    private String songName;
    private String singer;
    private String albumName;
    private List<String> userEmails; // List of user emails to notify
}




