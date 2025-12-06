<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Music Library</title>
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webjars/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Poppins', sans-serif; }
        body { background: linear-gradient(135deg, #F5F818 0%, #e4e8ec 100%); min-height: 100vh; }
        .hero { padding: 100px 0; text-align: center; }
        .hero h1 { font-size: 3.5rem; margin-bottom: 20px; color: #2c3e50; font-weight: 700; }
        .btn-custom { padding: 15px 40px; font-size: 1.1rem; border-radius: 50px; margin: 10px; font-weight: 500; transition: all 0.3s; }
        .btn-admin { background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%); border: none; color: white; }
        .btn-admin:hover { background: linear-gradient(135deg, #c0392b 0%, #a93226 100%); color: white; transform: translateY(-3px); box-shadow: 0 10px 30px rgba(231,76,60,0.3); }
        .btn-user { background: white; border: 2px solid #3498db; color: #3498db; }
        .btn-user:hover { background: #3498db; color: white; transform: translateY(-3px); box-shadow: 0 10px 30px rgba(52,152,219,0.3); }
        .feature-card { background: white; border-radius: 20px; padding: 40px 30px; margin: 15px 0; box-shadow: 0 10px 40px rgba(0,0,0,0.08); transition: all 0.3s; border: 1px solid #eee; }
        .feature-card:hover { transform: translateY(-10px); box-shadow: 0 20px 50px rgba(0,0,0,0.12); }
        .feature-icon { font-size: 3rem; color: #e74c3c; margin-bottom: 20px; }
        .feature-card h4 { color: #2c3e50; font-weight: 600; }
        .feature-card p { color: #0d6efd; }
        .music-icon { color: #e74c3c; }
    </style>
</head>
<body>
    <div class="hero">
        <i class="fas fa-music fa-4x mb-4 music-icon"></i>
        <h1>Music Library</h1>
        <p class="lead" style="color: #dc3545; font-size: 1.3rem;">Your Ultimate Music Management Platform</p>
        <div class="mt-5">
            <a href="/admin/login" class="btn btn-admin btn-custom">
                <i class="fas fa-user-shield me-2"></i>Admin Panel
            </a>
            <a href="/user/login" class="btn btn-user btn-custom">
                <i class="fas fa-user me-2"></i>User Portal
            </a>
        </div>
    </div>
    
    <div class="container pb-5">
        <div class="row">
            <div class="col-md-4">
                <div class="feature-card text-center">
                    <i class="fas fa-compact-disc feature-icon"></i>
                    <h4>Song Management</h4>
                    <p>Add and manage your music library with ease</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="feature-card text-center">
                    <i class="fas fa-list-ul feature-icon"></i>
                    <h4>Playlists</h4>
                    <p>Create and organize custom playlists</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="feature-card text-center">
                    <i class="fas fa-play-circle feature-icon"></i>
                    <h4>Music Player</h4>
                    <p>Play, shuffle, and repeat your favorites</p>
                </div>
            </div>
        </div>
    </div>
    
    <script src="/webjars/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>
