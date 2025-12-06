<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Notifications</title>
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webjars/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Poppins', sans-serif; }
        body { background: #f5f7fa; min-height: 100vh; }
        .sidebar { background: white; min-height: 100vh; padding: 20px; position: fixed; width: 260px; box-shadow: 2px 0 10px rgba(0,0,0,0.05); border-right: 1px solid #eee; }
        .sidebar-brand { font-size: 1.4rem; color: #e74c3c; padding: 20px 0; border-bottom: 1px solid #eee; font-weight: 600; }
        .nav-link { color: #5a6a7a; padding: 12px 15px; border-radius: 10px; margin: 5px 0; font-weight: 500; transition: all 0.3s; }
        .nav-link:hover, .nav-link.active { background: #e74c3c; color: white; }
        .nav-link i { width: 25px; }
        .main-content { margin-left: 260px; padding: 30px; }
        .notification-card { background: white; border-radius: 15px; padding: 20px; margin-bottom: 15px; box-shadow: 0 5px 20px rgba(0,0,0,0.05); border: 1px solid #eee; border-left: 4px solid #e74c3c; }
        .notification-card h5 { color: #2c3e50; font-weight: 600; }
        .notification-card p { color: #5a6a7a; }
        h2 { color: #2c3e50; }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-brand"><i class="fas fa-music me-2"></i>Music Library</div>
        <nav class="nav flex-column mt-4">
            <a class="nav-link" href="/admin"><i class="fas fa-tachometer-alt me-2"></i>Dashboard</a>
            <a class="nav-link" href="/admin/songs"><i class="fas fa-compact-disc me-2"></i>Songs</a>
            <a class="nav-link" href="/admin/songs/add"><i class="fas fa-plus-circle me-2"></i>Add Song</a>
            <a class="nav-link active" href="/admin/notifications"><i class="fas fa-bell me-2"></i>Notifications</a>
            <hr style="border-color: #eee;">
            <a class="nav-link" href="/"><i class="fas fa-home me-2"></i>Back to Home</a>
            <a class="nav-link" href="/admin/logout"><i class="fas fa-sign-out-alt me-2"></i>Logout</a>
        </nav>
    </div>
    
    <div class="main-content">
        <h2 class="mb-4"><i class="fas fa-bell me-2" style="color: #e74c3c;"></i>Notifications</h2>
        
        <c:forEach items="${notifications}" var="n">
            <div class="notification-card">
                <h5><i class="fas fa-music me-2" style="color: #e74c3c;"></i>${n.songName}</h5>
                <p>${n.message}</p>
                <small style="color: #7f8c8d;"><i class="fas fa-clock me-1"></i>${n.createdAt}</small>
                <span class="badge bg-success ms-2">${n.status}</span>
            </div>
        </c:forEach>
        
        <c:if test="${empty notifications}">
            <div class="text-center py-5" style="color: #7f8c8d;">
                <i class="fas fa-bell-slash fa-4x mb-3"></i>
                <p>No notifications yet</p>
            </div>
        </c:if>
    </div>
</body>
</html>
