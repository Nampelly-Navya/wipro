package com.musiclibrary.admin.feign;

import com.musiclibrary.admin.dto.SongDTO;
import com.musiclibrary.admin.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceClientFallback implements UserServiceClient {
    
    @Override
    public ResponseEntity<String> syncSong(SongDTO songDTO) {
        return ResponseEntity.ok("User service unavailable - sync queued");
    }
    
    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(new ArrayList<>());
    }
    
    @Override
    public ResponseEntity<String> deleteSong(Long songId) {
        return ResponseEntity.ok("User service unavailable - delete queued");
    }
}
