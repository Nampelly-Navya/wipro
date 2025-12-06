package com.musiclibrary.user.controller;

import com.musiclibrary.user.dto.PlayerResponseDTO;
import com.musiclibrary.user.security.JwtTokenProvider;
import com.musiclibrary.user.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/player")
@RequiredArgsConstructor
@Tag(name = "Player", description = "Music player control APIs")
public class PlayerController {
    
    private final PlayerService playerService;
    private final JwtTokenProvider jwtTokenProvider;
    
    @PostMapping("/play/{songId}")
    @Operation(summary = "Play song")
    public ResponseEntity<PlayerResponseDTO> play(@PathVariable Long songId) {
        return ResponseEntity.ok(playerService.play(songId));
    }
    
    @PostMapping("/stop")
    @Operation(summary = "Stop playback")
    public ResponseEntity<PlayerResponseDTO> stop() {
        return ResponseEntity.ok(playerService.stop());
    }
    
    @PostMapping("/shuffle/{playlistId}")
    @Operation(summary = "Shuffle playlist")
    public ResponseEntity<PlayerResponseDTO> shuffle(@PathVariable Long playlistId,
                                                      @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(playerService.shuffle(playlistId, userId));
    }
    
    @PostMapping("/repeat/{songId}")
    @Operation(summary = "Repeat song")
    public ResponseEntity<PlayerResponseDTO> repeat(@PathVariable Long songId) {
        return ResponseEntity.ok(playerService.repeat(songId));
    }
}
