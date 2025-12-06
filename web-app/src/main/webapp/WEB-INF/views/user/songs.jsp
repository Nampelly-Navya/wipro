<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Browse Songs</title>
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webjars/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Poppins', sans-serif; }
        body { background: #f5f7fa; min-height: 100vh; }
        .sidebar { background: white; min-height: 100vh; padding: 20px; position: fixed; width: 260px; box-shadow: 2px 0 10px rgba(0,0,0,0.05); border-right: 1px solid #eee; }
        .sidebar-brand { font-size: 1.4rem; color: #667eea; padding: 20px 0; border-bottom: 1px solid #eee; font-weight: 600; }
        .nav-link { color: #5a6a7a; padding: 12px 15px; border-radius: 10px; margin: 5px 0; font-weight: 500; transition: all 0.3s; }
        .nav-link:hover, .nav-link.active { background: #667eea; color: white; }
        .nav-link i { width: 25px; }
        .main-content { margin-left: 260px; padding: 30px; }
        
        .song-card { 
            background: white; 
            border-radius: 20px; 
            padding: 20px; 
            margin-bottom: 15px; 
            box-shadow: 0 5px 20px rgba(0,0,0,0.05); 
            border: 1px solid #eee; 
            transition: all 0.3s;
            display: flex;
            align-items: center;
        }
        .song-card:hover { 
            transform: translateY(-3px); 
            box-shadow: 0 15px 40px rgba(102, 126, 234, 0.15);
            border-color: #667eea;
        }
        .song-card h5 { color: #2c3e50; font-weight: 600; margin-bottom: 5px; }
        .song-card p { color: #7f8c8d; margin-bottom: 0; }
        
        .song-icon { 
            width: 60px; 
            height: 60px; 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); 
            border-radius: 15px; 
            display: flex; 
            align-items: center; 
            justify-content: center;
            color: white;
            font-size: 1.5rem;
            margin-right: 20px;
            flex-shrink: 0;
        }
        
        .song-cover {
            width: 60px;
            height: 60px;
            border-radius: 15px;
            object-fit: cover;
            margin-right: 20px;
        }
        
        .search-box { 
            background: white; 
            border: 2px solid #e9ecef; 
            color: #2c3e50; 
            padding: 15px 25px; 
            border-radius: 30px;
            font-size: 1rem;
        }
        .search-box:focus { 
            background: white; 
            color: #2c3e50; 
            border-color: #667eea; 
            box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1); 
        }
        
        h2 { color: #2c3e50; font-weight: 600; }
        
        .play-btn {
            width: 45px;
            height: 45px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 50%;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 0.3s;
            cursor: pointer;
        }
        .play-btn:hover {
            transform: scale(1.1);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
        }
        .play-btn:disabled {
            background: #bdc3c7;
            cursor: not-allowed;
        }
        .play-btn:disabled:hover {
            transform: none;
            box-shadow: none;
        }
        
        .no-audio-text {
            font-size: 0.75rem;
            color: #e74c3c;
            margin-top: 3px;
        }
        
        .audio-status {
            font-size: 0.7rem;
            padding: 3px 8px;
            border-radius: 10px;
            margin-left: 10px;
        }
        .audio-yes { background: #d4edda; color: #155724; }
        .audio-no { background: #fff3cd; color: #856404; }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-brand"><i class="fas fa-music me-2"></i>Music Library</div>
        <nav class="nav flex-column mt-4">
            <a class="nav-link" href="/user/dashboard"><i class="fas fa-tachometer-alt me-2"></i>Dashboard</a>
            <a class="nav-link active" href="/user/songs"><i class="fas fa-music me-2"></i>Browse Songs</a>
            <a class="nav-link" href="/user/playlists"><i class="fas fa-list me-2"></i>My Playlists</a>
            <a class="nav-link" href="/user/player"><i class="fas fa-play-circle me-2"></i>Player</a>
            <hr style="border-color: #eee;">
            <a class="nav-link" href="/"><i class="fas fa-home me-2"></i>Back to Home</a>
            <a class="nav-link" href="/user/logout"><i class="fas fa-sign-out-alt me-2"></i>Logout</a>
        </nav>
    </div>
    
    <div class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-music me-2" style="color: #667eea;"></i>Browse Songs</h2>
            <a href="/user/player" class="btn btn-primary" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border: none;">
                <i class="fas fa-play me-2"></i>Open Player
            </a>
        </div>
        
        <form action="/user/songs" method="get" class="mb-4">
            <div class="input-group">
                <input type="text" class="form-control search-box" name="search" placeholder="Search songs by name, singer, album..." value="${search}">
                <button type="submit" class="btn btn-primary ms-2" style="border-radius: 25px; padding: 0 25px; background: #667eea; border: none;">
                    <i class="fas fa-search"></i>
                </button>
                <c:if test="${not empty search}">
                    <a href="/user/songs" class="btn btn-secondary ms-2" style="border-radius: 25px; padding: 0 20px;">
                        <i class="fas fa-times"></i>
                    </a>
                </c:if>
            </div>
        </form>
        
        <c:forEach items="${songs}" var="song">
            <div class="song-card">
                <c:choose>
                    <c:when test="${not empty song.coverImageUrl}">
                        <img src="/user/songs/${song.songId}/cover" class="song-cover" alt="Cover" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                        <div class="song-icon" style="display: none;"><i class="fas fa-music"></i></div>
                    </c:when>
                    <c:otherwise>
                        <div class="song-icon"><i class="fas fa-music"></i></div>
                    </c:otherwise>
                </c:choose>
                
                <div class="flex-grow-1">
                    <h5>
                        ${song.songName}
                        <c:choose>
                            <c:when test="${not empty song.audioUrl}">
                                <span class="audio-status audio-yes"><i class="fas fa-volume-up me-1"></i>Audio</span>
                            </c:when>
                            <c:otherwise>
                                <span class="audio-status audio-no"><i class="fas fa-volume-mute me-1"></i>No Audio</span>
                            </c:otherwise>
                        </c:choose>
                    </h5>
                    <p><i class="fas fa-microphone me-1"></i>${song.singer} &bull; <i class="fas fa-compact-disc me-1"></i>${song.albumName}</p>
                </div>
                
                <span class="badge ${song.songType == 'PREMIUM' ? 'bg-warning text-dark' : 'bg-success'} me-3" style="font-size: 0.8rem; padding: 8px 15px;">
                    <i class="fas ${song.songType == 'PREMIUM' ? 'fa-crown' : 'fa-check'} me-1"></i>${song.songType}
                </span>
                
                <c:choose>
                    <c:when test="${not empty song.audioUrl}">
                        <a href="/user/player?play=${song.songId}" class="play-btn" title="Play this song">
                            <i class="fas fa-play"></i>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <button class="play-btn" disabled title="No audio available">
                            <i class="fas fa-play"></i>
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:forEach>
        
        <c:if test="${empty songs}">
            <div class="text-center py-5" style="color: #7f8c8d;">
                <i class="fas fa-music fa-4x mb-3" style="color: #ddd;"></i>
                <h4>No songs found</h4>
                <p>Try a different search term</p>
            </div>
        </c:if>
    </div>
</body>
</html>
