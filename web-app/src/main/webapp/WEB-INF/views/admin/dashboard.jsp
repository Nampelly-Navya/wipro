<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
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
        .stat-card { background: white; border-radius: 15px; padding: 25px; box-shadow: 0 5px 20px rgba(0,0,0,0.05); border: 1px solid #eee; }
        .stat-card h6 { color: #7f8c8d; font-weight: 500; }
        .stat-card h2 { color: #2c3e50; font-weight: 700; }
        .quick-action { background: white; border-radius: 15px; padding: 25px; text-align: center; text-decoration: none; display: block; box-shadow: 0 5px 20px rgba(0,0,0,0.05); border: 1px solid #eee; transition: all 0.3s; }
        .quick-action:hover { background: #e74c3c; color: white; transform: translateY(-5px); box-shadow: 0 10px 30px rgba(231,76,60,0.2); }
        .quick-action:hover i, .quick-action:hover span { color: white; }
        .quick-action i { color: #e74c3c; }
        .quick-action span { color: #2c3e50; font-weight: 500; }
        h2, h5 { color: #2c3e50; }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-brand"><i class="fas fa-music me-2"></i>Music Library</div>
        <nav class="nav flex-column mt-4">
            <a class="nav-link active" href="/admin"><i class="fas fa-tachometer-alt me-2"></i>Dashboard</a>
            <a class="nav-link" href="/admin/songs"><i class="fas fa-compact-disc me-2"></i>Songs</a>
            <a class="nav-link" href="/admin/songs/add"><i class="fas fa-plus-circle me-2"></i>Add Song</a>
            <a class="nav-link" href="/admin/notifications"><i class="fas fa-bell me-2"></i>Notifications</a>
            <hr style="border-color: #eee;">
            <a class="nav-link" href="/"><i class="fas fa-home me-2"></i>Back to Home</a>
            <a class="nav-link" href="/admin/logout"><i class="fas fa-sign-out-alt me-2"></i>Logout</a>
        </nav>
    </div>
    
    <div class="main-content">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-tachometer-alt me-2" style="color: #e74c3c;"></i>Admin Dashboard</h2>
            <span style="color: #7f8c8d;">Welcome, <strong style="color: #2c3e50;">${adminName}</strong></span>
        </div>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        
        <div class="row mb-4">
            <div class="col-md-4">
                <div class="stat-card">
                    <h6><i class="fas fa-compact-disc me-2" style="color: #e74c3c;"></i>Total Songs</h6>
                    <h2>${songCount}</h2>
                </div>
            </div>
        </div>
        
        <h5 class="mb-3"><i class="fas fa-bolt me-2" style="color: #e74c3c;"></i>Quick Actions</h5>
        <div class="row">
            <div class="col-md-3 mb-3">
                <a href="/admin/songs/add" class="quick-action">
                    <i class="fas fa-plus-circle fa-2x mb-2"></i><br>
                    <span>Add Song</span>
                </a>
            </div>
            <div class="col-md-3 mb-3">
                <a href="/admin/songs" class="quick-action">
                    <i class="fas fa-list fa-2x mb-2"></i><br>
                    <span>View Songs</span>
                </a>
            </div>
            <div class="col-md-3 mb-3">
                <a href="/admin/notifications" class="quick-action">
                    <i class="fas fa-bell fa-2x mb-2"></i><br>
                    <span>Notifications</span>
                </a>
            </div>
        </div>
    </div>
</body>
</html>
