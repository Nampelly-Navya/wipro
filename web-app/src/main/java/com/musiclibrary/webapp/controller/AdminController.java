package com.musiclibrary.webapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.*;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    @Qualifier("apiGatewayUrl")
    private String apiUrl;
    
    @Autowired
    @Qualifier("adminServiceUrl")
    private String adminUrl;  // Direct admin-service URL for file uploads
    
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("adminId") != null) return "redirect:/admin";
        return "admin/login";
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
            
            ResponseEntity<Map> resp = restTemplate.postForEntity(apiUrl + "/admin/auth/login", entity, Map.class);
            Map<String, Object> body = resp.getBody();
            
            if (body != null && body.get("adminId") != null) {
                session.setAttribute("adminId", body.get("adminId"));
                session.setAttribute("adminName", body.get("adminName"));
                session.setAttribute("adminUsername", body.get("username"));
                return "redirect:/admin";
            }
            model.addAttribute("error", "Login failed");
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Invalid credentials");
        } catch (Exception e) {
            model.addAttribute("error", "Cannot connect to server");
        }
        return "admin/login";
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "admin/register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam Map<String, String> data, Model model) {
        try {
            Map<String, Object> admin = new HashMap<>();
            admin.put("adminName", data.get("adminName"));
            admin.put("username", data.get("username"));
            admin.put("password", data.get("password"));
            admin.put("email", data.get("email"));
            admin.put("mobile", data.get("mobile"));
            admin.put("adminStatus", "ACTIVE");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(admin, headers);
            
            restTemplate.postForEntity(apiUrl + "/admin/auth/register", entity, Map.class);
            return "redirect:/admin/login?success=Registration successful! Welcome email sent.";
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "Registration failed - username or email already exists");
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed");
        }
        return "admin/register";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login?success=Logged out";
    }
    
    @GetMapping("")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("adminId") == null) return "redirect:/admin/login";
        model.addAttribute("adminName", session.getAttribute("adminName"));
        try {
            ResponseEntity<List> resp = restTemplate.getForEntity(apiUrl + "/admin/songs", List.class);
            model.addAttribute("songs", resp.getBody());
            model.addAttribute("songCount", resp.getBody() != null ? resp.getBody().size() : 0);
        } catch (Exception e) {
            model.addAttribute("songs", new ArrayList<>());
            model.addAttribute("songCount", 0);
            model.addAttribute("error", "Cannot connect to Admin Service");
        }
        return "admin/dashboard";
    }
    
    @GetMapping("/songs")
    public String listSongs(Model model) {
        try {
            ResponseEntity<List> resp = restTemplate.getForEntity(apiUrl + "/admin/songs", List.class);
            model.addAttribute("songs", resp.getBody());
        } catch (Exception e) {
            model.addAttribute("songs", new ArrayList<>());
            model.addAttribute("error", "Cannot fetch songs");
        }
        return "admin/songs";
    }
    
    @GetMapping("/songs/add")
    public String addSongForm() {
        return "admin/add-song";
    }
    
    @PostMapping("/songs/add")
    public String addSong(@RequestParam Map<String, String> data,
                          @RequestParam(value = "audioFile", required = false) MultipartFile audioFile,
                          @RequestParam(value = "coverFile", required = false) MultipartFile coverFile,
                          Model model) {
        try {
            // Step 1: Create song with metadata
            Map<String, Object> song = new HashMap<>();
            song.put("songName", data.get("songName"));
            song.put("musicDirector", data.get("musicDirector"));
            song.put("singer", data.get("singer"));
            song.put("releaseDate", data.get("releaseDate"));
            song.put("albumName", data.get("albumName"));
            song.put("songType", data.get("songType"));
            song.put("songStatus", data.get("songStatus"));
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> req = new HttpEntity<>(song, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl + "/admin/songs", req, Map.class);
            Map<String, Object> savedSong = response.getBody();
            Long songId = ((Number) savedSong.get("songId")).longValue();
            
            // Step 2: Upload audio file if provided
            if (audioFile != null && !audioFile.isEmpty()) {
                uploadFileToAdmin(songId, audioFile, "audio");
            }
            
            // Step 3: Upload cover file if provided
            if (coverFile != null && !coverFile.isEmpty()) {
                uploadFileToAdmin(songId, coverFile, "cover");
            }
            
            return "redirect:/admin/songs?success=Song added successfully";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add song: " + e.getMessage());
            return "admin/add-song";
        }
    }
    
    private void uploadFileToAdmin(Long songId, MultipartFile file, String type) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
            
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            String endpoint = type.equals("audio") ? "/admin/songs/" + songId + "/upload-audio" 
                                                   : "/admin/songs/" + songId + "/upload-cover";
            // Use adminUrl directly (bypass API Gateway to avoid 413 error)
            restTemplate.postForEntity(adminUrl + endpoint, requestEntity, Map.class);
            System.out.println("✅ Uploaded " + type + " file for song " + songId);
        } catch (Exception e) {
            // Log but don't fail the whole operation
            System.err.println("Failed to upload " + type + " file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @PostMapping("/songs/{id}/upload-audio")
    public String uploadAudio(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            uploadFileToAdmin(id, file, "audio");
            return "redirect:/admin/songs/edit/" + id + "?success=Audio uploaded successfully";
        } catch (Exception e) {
            return "redirect:/admin/songs/edit/" + id + "?error=Failed to upload audio";
        }
    }
    
    @PostMapping("/songs/{id}/upload-cover")
    public String uploadCover(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            uploadFileToAdmin(id, file, "cover");
            return "redirect:/admin/songs/edit/" + id + "?success=Cover uploaded successfully";
        } catch (Exception e) {
            return "redirect:/admin/songs/edit/" + id + "?error=Failed to upload cover";
        }
    }
    
    @GetMapping("/songs/audio-stream/{id}")
    public ResponseEntity<Resource> streamAudio(@PathVariable Long id) {
        try {
            ResponseEntity<byte[]> response = restTemplate.getForEntity(
                apiUrl + "/admin/songs/" + id + "/stream", byte[].class);
            
            ByteArrayResource resource = new ByteArrayResource(response.getBody());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("audio/mpeg"))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/songs/cover-stream/{id}")
    public ResponseEntity<Resource> streamCover(@PathVariable Long id) {
        try {
            ResponseEntity<byte[]> response = restTemplate.getForEntity(
                apiUrl + "/admin/songs/" + id + "/cover-image", byte[].class);
            
            ByteArrayResource resource = new ByteArrayResource(response.getBody());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/songs/edit/{id}")
    public String editSongForm(@PathVariable Long id, Model model) {
        try {
            ResponseEntity<Map> resp = restTemplate.getForEntity(apiUrl + "/admin/songs/" + id, Map.class);
            model.addAttribute("song", resp.getBody());
        } catch (Exception e) {
            return "redirect:/admin/songs?error=Song not found";
        }
        return "admin/edit-song";
    }
    
    @PostMapping("/songs/edit/{id}")
    public String updateSong(@PathVariable Long id, @RequestParam Map<String, String> data) {
        try {
            Map<String, Object> song = new HashMap<>();
            song.put("songName", data.get("songName"));
            song.put("musicDirector", data.get("musicDirector"));
            song.put("singer", data.get("singer"));
            song.put("releaseDate", data.get("releaseDate"));
            song.put("albumName", data.get("albumName"));
            song.put("songType", data.get("songType"));
            song.put("songStatus", data.get("songStatus"));
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> req = new HttpEntity<>(song, headers);
            
            restTemplate.exchange(apiUrl + "/admin/songs/" + id, HttpMethod.PUT, req, Map.class);
            return "redirect:/admin/songs?success=Song updated";
        } catch (Exception e) {
            return "redirect:/admin/songs?error=Failed to update";
        }
    }
    
    @GetMapping("/songs/delete/{id}")
    public String deleteSong(@PathVariable Long id) {
        try {
            restTemplate.delete(apiUrl + "/admin/songs/" + id);
        } catch (Exception e) {
            return "redirect:/admin/songs?error=Failed to delete";
        }
        return "redirect:/admin/songs?success=Song deleted";
    }
    
    @GetMapping("/songs/status/{id}")
    public String toggleStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            restTemplate.put(apiUrl + "/admin/songs/" + id + "/status?status=" + status, null);
        } catch (Exception e) {
            return "redirect:/admin/songs?error=Failed to update status";
        }
        return "redirect:/admin/songs?success=Status updated";
    }
    
    @GetMapping("/songs/sync-all")
    public String syncAllSongs() {
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> resp = restTemplate.postForEntity(apiUrl + "/admin/songs/sync-all", entity, String.class);
            return "redirect:/admin/songs?success=" + resp.getBody();
        } catch (Exception e) {
            return "redirect:/admin/songs?error=Failed to sync songs: " + e.getMessage();
        }
    }
    
    @GetMapping("/notifications")
    public String notifications(Model model) {
        try {
            ResponseEntity<List> resp = restTemplate.getForEntity(apiUrl + "/notify/all", List.class);
            model.addAttribute("notifications", resp.getBody());
        } catch (Exception e) {
            model.addAttribute("notifications", new ArrayList<>());
        }
        return "admin/notifications";
    }
}
