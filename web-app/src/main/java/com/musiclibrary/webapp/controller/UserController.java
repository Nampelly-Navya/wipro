package com.musiclibrary.webapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    @Qualifier("apiGatewayUrl")
    private String apiUrl;
    
    @Autowired
    @Qualifier("adminServiceUrl")
    private String adminUrl;
    
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("token") != null) return "redirect:/user/dashboard";
        return "user/login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, 
                        HttpSession session, Model model) {
        try {
            Map<String, String> req = new HashMap<>();
            req.put("username", username);
            req.put("password", password);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(req, headers);
            
            ResponseEntity<Map> resp = restTemplate.postForEntity(apiUrl + "/api/auth/login", entity, Map.class);
            Map<String, Object> body = resp.getBody();
            
            if (body != null && body.get("token") != null) {
                session.setAttribute("token", body.get("token"));
                session.setAttribute("userId", body.get("userId"));
                session.setAttribute("username", body.get("username"));
                return "redirect:/user/dashboard";
            }
            model.addAttribute("error", "Login failed");
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Invalid credentials");
        } catch (Exception e) {
            model.addAttribute("error", "Cannot connect to server");
        }
        return "user/login";
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "user/register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam Map<String, String> data, Model model) {
        try {
            Map<String, Object> user = new HashMap<>();
            user.put("firstName", data.get("firstName"));
            user.put("lastName", data.get("lastName"));
            user.put("username", data.get("username"));
            user.put("password", data.get("password"));
            user.put("email", data.get("email"));
            user.put("mobile", data.get("mobile"));
            user.put("address1", data.get("address1"));
            user.put("address2", data.get("address2"));
            user.put("city", data.get("city"));
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(user, headers);
            
            restTemplate.postForEntity(apiUrl + "/api/auth/register", entity, Map.class);
            return "redirect:/user/login?success=Registration successful";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed");
            return "user/register";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login?success=Logged out";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAuth(session)) return "redirect:/user/login";
        model.addAttribute("username", session.getAttribute("username"));
        
        try {
            HttpEntity<String> entity = authEntity(session);
            ResponseEntity<List> songs = restTemplate.exchange(
                apiUrl + "/api/user/songs", HttpMethod.GET, entity, List.class);
            ResponseEntity<List> playlists = restTemplate.exchange(
                apiUrl + "/api/playlists", HttpMethod.GET, entity, List.class);
            
            model.addAttribute("songCount", songs.getBody() != null ? songs.getBody().size() : 0);
            model.addAttribute("playlistCount", playlists.getBody() != null ? playlists.getBody().size() : 0);
        } catch (Exception e) {
            model.addAttribute("songCount", 0);
            model.addAttribute("playlistCount", 0);
        }
        return "user/dashboard";
    }
    
    @GetMapping("/songs")
    public String songs(HttpSession session, Model model, @RequestParam(required = false) String search) {
        if (!isAuth(session)) return "redirect:/user/login";
        
        try {
            String url = search != null ? apiUrl + "/api/user/songs/search?query=" + search 
                                        : apiUrl + "/api/user/songs";
            ResponseEntity<List> resp = restTemplate.exchange(url, HttpMethod.GET, authEntity(session), List.class);
            model.addAttribute("songs", resp.getBody());
            model.addAttribute("search", search);
        } catch (Exception e) {
            model.addAttribute("songs", new ArrayList<>());
        }
        return "user/songs";
    }
    
    @GetMapping("/playlists")
    public String playlists(HttpSession session, Model model) {
        if (!isAuth(session)) return "redirect:/user/login";
        
        try {
            ResponseEntity<List> resp = restTemplate.exchange(
                apiUrl + "/api/playlists", HttpMethod.GET, authEntity(session), List.class);
            model.addAttribute("playlists", resp.getBody());
        } catch (Exception e) {
            model.addAttribute("playlists", new ArrayList<>());
        }
        return "user/playlists";
    }
    
    @GetMapping("/playlists/create")
    public String createPlaylistForm(HttpSession session) {
        if (!isAuth(session)) return "redirect:/user/login";
        return "user/create-playlist";
    }
    
    @PostMapping("/playlists/create")
    public String createPlaylist(@RequestParam String playlistName, HttpSession session) {
        if (!isAuth(session)) return "redirect:/user/login";
        
        try {
            Map<String, String> playlist = new HashMap<>();
            playlist.put("playlistName", playlistName);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + session.getAttribute("token"));
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(playlist, headers);
            
            restTemplate.postForEntity(apiUrl + "/api/playlists", entity, Map.class);
            return "redirect:/user/playlists?success=Playlist created";
        } catch (Exception e) {
            return "redirect:/user/playlists?error=Failed to create";
        }
    }
    
    @GetMapping("/playlists/{id}")
    public String playlistDetail(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAuth(session)) return "redirect:/user/login";
        
        try {
            HttpEntity<String> entity = authEntity(session);
            ResponseEntity<Map> playlist = restTemplate.exchange(
                apiUrl + "/api/playlists/" + id, HttpMethod.GET, entity, Map.class);
            ResponseEntity<List> songs = restTemplate.exchange(
                apiUrl + "/api/playlists/" + id + "/songs", HttpMethod.GET, entity, List.class);
            ResponseEntity<List> allSongs = restTemplate.exchange(
                apiUrl + "/api/user/songs", HttpMethod.GET, entity, List.class);
            
            model.addAttribute("playlist", playlist.getBody());
            model.addAttribute("songs", songs.getBody());
            model.addAttribute("availableSongs", allSongs.getBody());
        } catch (Exception e) {
            return "redirect:/user/playlists?error=Playlist not found";
        }
        return "user/playlist-detail";
    }
    
    @PostMapping("/playlists/{id}/add-song")
    public String addSongToPlaylist(@PathVariable Long id, @RequestParam Long songId, HttpSession session) {
        if (!isAuth(session)) return "redirect:/user/login";
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + session.getAttribute("token"));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            restTemplate.postForEntity(apiUrl + "/api/playlists/" + id + "/songs?songId=" + songId, entity, String.class);
            return "redirect:/user/playlists/" + id + "?success=Song added";
        } catch (Exception e) {
            return "redirect:/user/playlists/" + id + "?error=Failed to add song";
        }
    }
    
    @GetMapping("/playlists/{id}/remove-song/{songId}")
    public String removeSong(@PathVariable Long id, @PathVariable Long songId, HttpSession session) {
        if (!isAuth(session)) return "redirect:/user/login";
        
        try {
            restTemplate.exchange(apiUrl + "/api/playlists/" + id + "/songs/" + songId, 
                HttpMethod.DELETE, authEntity(session), String.class);
            return "redirect:/user/playlists/" + id + "?success=Song removed";
        } catch (Exception e) {
            return "redirect:/user/playlists/" + id + "?error=Failed";
        }
    }
    
    @GetMapping("/playlists/{id}/delete")
    public String deletePlaylist(@PathVariable Long id, HttpSession session) {
        if (!isAuth(session)) return "redirect:/user/login";
        
        try {
            restTemplate.exchange(apiUrl + "/api/playlists/" + id, HttpMethod.DELETE, authEntity(session), String.class);
            return "redirect:/user/playlists?success=Playlist deleted";
        } catch (Exception e) {
            return "redirect:/user/playlists?error=Failed to delete";
        }
    }
    
    @GetMapping("/player")
    public String player(HttpSession session, Model model) {
        if (!isAuth(session)) return "redirect:/user/login";
        
        try {
            HttpEntity<String> entity = authEntity(session);
            ResponseEntity<List> songs = restTemplate.exchange(
                apiUrl + "/api/user/songs", HttpMethod.GET, entity, List.class);
            ResponseEntity<List> playlists = restTemplate.exchange(
                apiUrl + "/api/playlists", HttpMethod.GET, entity, List.class);
            
            model.addAttribute("songs", songs.getBody());
            model.addAttribute("playlists", playlists.getBody());
        } catch (Exception e) {
            model.addAttribute("songs", new ArrayList<>());
            model.addAttribute("playlists", new ArrayList<>());
        }
        return "user/player";
    }
    
    @PostMapping("/player/play/{songId}")
    @ResponseBody
    public Map<String, Object> play(@PathVariable Long songId, HttpSession session) {
        try {
            ResponseEntity<Map> resp = restTemplate.exchange(
                apiUrl + "/api/player/play/" + songId, HttpMethod.POST, authEntity(session), Map.class);
            return resp.getBody();
        } catch (Exception e) {
            return Map.of("status", "ERROR", "message", e.getMessage());
        }
    }
    
    @PostMapping("/player/stop")
    @ResponseBody
    public Map<String, Object> stop(HttpSession session) {
        try {
            ResponseEntity<Map> resp = restTemplate.exchange(
                apiUrl + "/api/player/stop", HttpMethod.POST, authEntity(session), Map.class);
            return resp.getBody();
        } catch (Exception e) {
            return Map.of("status", "ERROR", "message", e.getMessage());
        }
    }
    
    @PostMapping("/player/shuffle/{playlistId}")
    @ResponseBody
    public Map<String, Object> shuffle(@PathVariable Long playlistId, HttpSession session) {
        try {
            ResponseEntity<Map> resp = restTemplate.exchange(
                apiUrl + "/api/player/shuffle/" + playlistId, HttpMethod.POST, authEntity(session), Map.class);
            return resp.getBody();
        } catch (Exception e) {
            return Map.of("status", "ERROR", "message", e.getMessage());
        }
    }
    
    @GetMapping("/songs/{songId}/stream")
    public ResponseEntity<Resource> streamAudio(@PathVariable Long songId, HttpSession session) {
        if (!isAuth(session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        
        try {
            // Stream directly from admin-service (where files are stored) - bypass API gateway
            String streamUrl = adminUrl + "/admin/songs/audio/" + songId;
            System.out.println("Streaming audio from: " + streamUrl);
            
            ResponseEntity<byte[]> response = restTemplate.getForEntity(streamUrl, byte[].class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
                ByteArrayResource resource = new ByteArrayResource(response.getBody());
                System.out.println("Audio file size: " + response.getBody().length + " bytes");
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("audio/mpeg"))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                        .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                        .body(resource);
            }
            System.err.println("Audio not found or empty for song: " + songId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Audio stream error for song " + songId + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/songs/{songId}/cover")
    public ResponseEntity<Resource> getCoverImage(@PathVariable Long songId, HttpSession session) {
        if (!isAuth(session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        
        try {
            // Get cover directly from admin-service (where files are stored) - bypass API gateway
            String coverUrl = adminUrl + "/admin/songs/cover/" + songId;
            ResponseEntity<byte[]> response = restTemplate.getForEntity(coverUrl, byte[].class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
                ByteArrayResource resource = new ByteArrayResource(response.getBody());
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/player/repeat/{songId}")
    @ResponseBody
    public Map<String, Object> repeat(@PathVariable Long songId, HttpSession session) {
        return Map.of("status", "OK", "message", "Repeat mode enabled for song");
    }
    
    private boolean isAuth(HttpSession session) {
        return session.getAttribute("token") != null;
    }
    
    private HttpEntity<String> authEntity(HttpSession session) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + session.getAttribute("token"));
        return new HttpEntity<>(headers);
    }
}
