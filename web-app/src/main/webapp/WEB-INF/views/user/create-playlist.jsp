<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create Playlist</title>
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
        .form-card { background: white; border-radius: 15px; padding: 40px; max-width: 500px; box-shadow: 0 5px 20px rgba(0,0,0,0.05); border: 1px solid #eee; }
        .form-control { background: #f8f9fa; border: 2px solid #e9ecef; color: #2c3e50; padding: 15px; border-radius: 10px; }
        .form-control:focus { background: white; color: #2c3e50; border-color: #3498db; box-shadow: 0 0 0 3px rgba(52,152,219,0.1); }
        .form-label { color: #2c3e50; font-weight: 500; }
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
        <h2 class="mb-4"><i class="fas fa-plus-circle me-2" style="color: #3498db;"></i>Create Playlist</h2>
        
        <div class="form-card">
            <div class="text-center mb-4">
                <i class="fas fa-list-ul fa-3x" style="color: #3498db;"></i>
            </div>
            <form action="/user/playlists/create" method="post">
                <div class="mb-4">
                    <label class="form-label">Playlist Name</label>
                    <input type="text" class="form-control" name="playlistName" placeholder="Enter playlist name..." required>
                </div>
                <button type="submit" class="btn btn-primary w-100"><i class="fas fa-plus me-2"></i>Create Playlist</button>
            </form>
            <div class="text-center mt-3">
                <a href="/user/playlists" style="color: #7f8c8d;"><i class="fas fa-arrow-left me-1"></i>Back to Playlists</a>
            </div>
        </div>
    </div>
</body>
</html>
