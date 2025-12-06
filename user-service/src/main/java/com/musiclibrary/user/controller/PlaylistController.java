package com.musiclibrary.user.controller;

import com.musiclibrary.user.dto.PlaylistDTO;
import com.musiclibrary.user.dto.SongDTO;
import com.musiclibrary.user.security.JwtTokenProvider;
import com.musiclibrary.user.service.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
@Tag(name = "Playlists", description = "Playlist management APIs")
public class PlaylistController {
    
    private final PlaylistService playlistService;
    private final JwtTokenProvider jwtTokenProvider;
    
    @PostMapping
    @Operation(summary = "Create playlist")
    public ResponseEntity<PlaylistDTO> createPlaylist(@Valid @RequestBody PlaylistDTO dto,
                                                       @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(playlistService.createPlaylist(dto, getUserId(token)), HttpStatus.CREATED);
    }
    
    @GetMapping
    @Operation(summary = "Get user playlists")
    public ResponseEntity<List<PlaylistDTO>> getUserPlaylists(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(playlistService.getUserPlaylists(getUserId(token)));
    }
    
    @GetMapping("/{playlistId}")
    @Operation(summary = "Get playlist by ID")
    public ResponseEntity<PlaylistDTO> getPlaylistById(@PathVariable Long playlistId,
                                                        @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(playlistService.getPlaylistById(playlistId, getUserId(token)));
    }
    
    @PutMapping("/{playlistId}")
    @Operation(summary = "Update playlist")
    public ResponseEntity<PlaylistDTO> updatePlaylist(@PathVariable Long playlistId,
                                                       @Valid @RequestBody PlaylistDTO dto,
                                                       @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(playlistService.updatePlaylist(playlistId, dto, getUserId(token)));
    }
    
    @DeleteMapping("/{playlistId}")
    @Operation(summary = "Delete playlist")
    public ResponseEntity<String> deletePlaylist(@PathVariable Long playlistId,
                                                  @RequestHeader("Authorization") String token) {
        playlistService.deletePlaylist(playlistId, getUserId(token));
        return ResponseEntity.ok("Playlist deleted");
    }
    
    @PostMapping("/{playlistId}/songs")
    @Operation(summary = "Add song to playlist")
    public ResponseEntity<String> addSong(@PathVariable Long playlistId, @RequestParam Long songId,
                                           @RequestHeader("Authorization") String token) {
        playlistService.addSongToPlaylist(playlistId, songId, getUserId(token));
        return ResponseEntity.ok("Song added to playlist");
    }
    
    @GetMapping("/{playlistId}/songs")
    @Operation(summary = "Get playlist songs")
    public ResponseEntity<List<SongDTO>> getPlaylistSongs(@PathVariable Long playlistId,
                                                           @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(playlistService.getPlaylistSongs(playlistId, getUserId(token)));
    }
    
    @DeleteMapping("/{playlistId}/songs/{songId}")
    @Operation(summary = "Remove song from playlist")
    public ResponseEntity<String> removeSong(@PathVariable Long playlistId, @PathVariable Long songId,
                                              @RequestHeader("Authorization") String token) {
        playlistService.removeSongFromPlaylist(playlistId, songId, getUserId(token));
        return ResponseEntity.ok("Song removed from playlist");
    }
    
    @GetMapping("/{playlistId}/songs/search")
    @Operation(summary = "Search songs in playlist")
    public ResponseEntity<List<SongDTO>> searchPlaylistSongs(@PathVariable Long playlistId,
                                                              @RequestParam String query,
                                                              @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(playlistService.searchPlaylistSongs(playlistId, query, getUserId(token)));
    }
    
    private Long getUserId(String token) {
        return jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
    }
}
