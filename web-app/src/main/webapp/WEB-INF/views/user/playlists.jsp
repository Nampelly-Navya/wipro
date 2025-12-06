<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Playlists</title>
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
        .playlist-card { background: white; border-radius: 15px; padding: 25px; margin-bottom: 20px; box-shadow: 0 5px 20px rgba(0,0,0,0.05); border: 1px solid #eee; transition: all 0.3s; }
        .playlist-card:hover { transform: translateY(-5px); box-shadow: 0 10px 30px rgba(0,0,0,0.08); }
        .playlist-card h5 { color: #2c3e50; font-weight: 600; }
        .playlist-icon { width: 60px; height: 60px; background: linear-gradient(135deg, #3498db 0%, #2980b9 100%); border-radius: 15px; display: flex; align-items: center; justify-content: center; }
        h2 { color: #2c3e50; font-weight: 600; }
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
        <div class="d-flex justify-content-between mb-4">
            <h2><i class="fas fa-list me-2" style="color: #3498db;"></i>My Playlists</h2>
            <a href="/user/playlists/create" class="btn btn-primary"><i class="fas fa-plus me-2"></i>Create Playlist</a>
        </div>
        
        <c:if test="${param.success != null}"><div class="alert alert-success">${param.success}</div></c:if>
        <c:if test="${param.error != null}"><div class="alert alert-danger">${param.error}</div></c:if>
        
        <div class="row">
            <c:forEach items="${playlists}" var="playlist">
                <div class="col-md-6 col-lg-4">
                    <div class="playlist-card">
                        <div class="d-flex align-items-center mb-3">
                            <div class="playlist-icon me-3"><i class="fas fa-list-ul fa-lg text-white"></i></div>
                            <div>
                                <h5 class="mb-0">${playlist.playlistName}</h5>
                                <small style="color: #7f8c8d;">Playlist</small>
                            </div>
                        </div>
                        <div class="d-flex gap-2">
                            <a href="/user/playlists/${playlist.playlistId}" class="btn btn-sm btn-primary flex-grow-1"><i class="fas fa-eye me-1"></i>View</a>
                            <a href="/user/playlists/${playlist.playlistId}/delete" class="btn btn-sm btn-danger" onclick="return confirm('Delete?')"><i class="fas fa-trash"></i></a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        
        <c:if test="${empty playlists}">
            <div class="text-center py-5" style="color: #7f8c8d;">
                <i class="fas fa-list fa-4x mb-3"></i>
                <p>No playlists yet</p>
                <a href="/user/playlists/create" class="btn btn-primary"><i class="fas fa-plus me-2"></i>Create Playlist</a>
            </div>
        </c:if>
    </div>
</body>
</html>
