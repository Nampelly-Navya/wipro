<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${playlist.playlistName}</title>
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webjars/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Poppins', sans-serif; }
        body { background: #f5f7fa; min-height: 100vh; }
        .sidebar { background: white; min-height: 100vh; padding: 20px; position: fixed; width: 260px; box-shadow: 2px 0 10px rgba(0,0,0,0.05); border-right: 1px solid #eee; }
        .sidebar-brand { font-size: 1.4rem; color: #3498db; padding: 20px 0; border-bottom: 1px solid #eee; font-weight: 600; }
        .nav-link { color: #5a6a7a; padding: 12px 15px; border-radius: 10px; margin: 5px 0; font-weight: 500; transition: all 0.3s; }
        .nav-link:hover, .nav-link.active { background: #3498db; color: white; }
        .nav-link i { width: 25px; }
        .main-content { margin-left: 260px; padding: 30px; }
        .playlist-header { background: linear-gradient(135deg, #3498db 0%, #2980b9 100%); border-radius: 20px; padding: 40px; margin-bottom: 30px; color: white; }
        .song-item { background: white; border-radius: 10px; padding: 15px; margin-bottom: 10px; box-shadow: 0 3px 10px rgba(0,0,0,0.05); border: 1px solid #eee; }
        .song-item strong { color: #2c3e50; }
        .song-item p { color: #7f8c8d; }
        .form-select { background: #f8f9fa; border: 2px solid #e9ecef; color: #2c3e50; padding: 10px 15px; border-radius: 10px; }
        .form-select:focus { border-color: #3498db; box-shadow: 0 0 0 3px rgba(52,152,219,0.1); }
        .card { background: white; border: 1px solid #eee; border-radius: 15px; }
        h5 { color: #2c3e50; font-weight: 600; }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-brand"><i class="fas fa-music me-2"></i>Music Library</div>
        <nav class="nav flex-column mt-4">
            <a class="nav-link" href="/user/dashboard"><i class="fas fa-tachometer-alt me-2"></i>Dashboard</a>
            <a class="nav-link" href="/user/songs"><i class="fas fa-music me-2"></i>Browse Songs</a>
            <a class="nav-link active" href="/user/playlists"><i class="fas fa-list me-2"></i>My Playlists</a>
            <a class="nav-link" href="/user/player"><i class="fas fa-play-circle me-2"></i>Player</a>
            <hr style="border-color: #eee;">
            <a class="nav-link" href="/"><i class="fas fa-home me-2"></i>Back to Home</a>
            <a class="nav-link" href="/user/logout"><i class="fas fa-sign-out-alt me-2"></i>Logout</a>
        </nav>
    </div>
    
    <div class="main-content">
        <div class="playlist-header d-flex align-items-center">
            <i class="fas fa-list-ul fa-3x me-4"></i>
            <div>
                <h2 class="mb-1">${playlist.playlistName}</h2>
                <p class="mb-0 opacity-75">Your playlist</p>
            </div>
        </div>
        
        <c:if test="${param.success != null}"><div class="alert alert-success">${param.success}</div></c:if>
        <c:if test="${param.error != null}"><div class="alert alert-danger">${param.error}</div></c:if>
        
        <div class="card mb-4">
            <div class="card-body">
                <h5><i class="fas fa-plus-circle me-2" style="color: #3498db;"></i>Add Song</h5>
                <form action="/user/playlists/${playlist.playlistId}/add-song" method="post" class="d-flex gap-2">
                    <select name="songId" class="form-select" style="max-width: 400px;" required>
                        <option value="">Select a song...</option>
                        <c:forEach items="${availableSongs}" var="song">
                            <option value="${song.songId}">${song.songName} - ${song.singer}</option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-primary"><i class="fas fa-plus"></i> Add</button>
                </form>
            </div>
        </div>
        
        <h5 class="mb-3"><i class="fas fa-music me-2" style="color: #3498db;"></i>Songs in Playlist</h5>
        <c:forEach items="${songs}" var="song" varStatus="s">
            <div class="song-item d-flex align-items-center">
                <span class="me-3" style="color: #7f8c8d; min-width: 30px;">${s.index + 1}</span>
                <div class="flex-grow-1">
                    <strong>${song.songName}</strong>
                    <p class="mb-0 small">${song.singer} | ${song.albumName}</p>
                </div>
                <a href="/user/playlists/${playlist.playlistId}/remove-song/${song.songId}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Remove this song?')"><i class="fas fa-times"></i></a>
            </div>
        </c:forEach>
        
        <c:if test="${empty songs}">
            <div class="text-center py-4" style="color: #7f8c8d;">
                <i class="fas fa-music fa-3x mb-3"></i>
                <p>No songs in this playlist yet</p>
            </div>
        </c:if>
        
        <div class="mt-4">
            <a href="/user/playlists" class="btn btn-secondary"><i class="fas fa-arrow-left me-2"></i>Back to Playlists</a>
        </div>
    </div>
</body>
</html>
