package com.musiclibrary.admin.feign;

import com.musiclibrary.admin.dto.SongDTO;
import com.musiclibrary.admin.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "user-service", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {
    
    @PostMapping("/api/users/sync/song")
    ResponseEntity<String> syncSong(@RequestBody SongDTO songDTO);
    
    @GetMapping("/api/users/all")
    ResponseEntity<List<UserDTO>> getAllUsers();
    
    @DeleteMapping("/api/users/songs/{songId}")
    ResponseEntity<String> deleteSong(@PathVariable("songId") Long songId);
}
