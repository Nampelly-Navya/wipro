<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Music Library</title>
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webjars/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Poppins', sans-serif; }
        body { background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%); min-height: 100vh; display: flex; align-items: center; justify-content: center; }
        .login-card { background: white; border-radius: 20px; padding: 50px; max-width: 420px; width: 100%; box-shadow: 0 20px 60px rgba(0,0,0,0.1); border: 1px solid #eee; }
        .form-control { background: #f8f9fa; border: 2px solid #e9ecef; color: #2c3e50; padding: 15px; border-radius: 10px; }
        .form-control:focus { background: white; color: #2c3e50; border-color: #3498db; box-shadow: 0 0 0 3px rgba(52,152,219,0.1); }
        .form-label { color: #2c3e50; font-weight: 500; }
        .btn-login { background: linear-gradient(135deg, #3498db 0%, #2980b9 100%); border: none; padding: 15px; width: 100%; border-radius: 10px; font-weight: 600; color: white; }
        .btn-login:hover { background: linear-gradient(135deg, #2980b9 0%, #1f6dad 100%); color: white; }
        h3 { color: #2c3e50; font-weight: 600; }
        a { color: #3498db; text-decoration: none; font-weight: 500; }
        a:hover { color: #2980b9; }
    </style>
</head>
<body>
    <div class="login-card">
        <div class="text-center mb-4">
            <i class="fas fa-music fa-3x" style="color: #3498db;"></i>
            <h3 class="mt-3">Welcome Back</h3>
        </div>
        
        <c:if test="${param.success != null}"><div class="alert alert-success">${param.success}</div></c:if>
        <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
        
        <form action="/user/login" method="post">
            <div class="mb-3">
                <label class="form-label"><i class="fas fa-user me-2"></i>Username</label>
                <input type="text" class="form-control" name="username" placeholder="Enter your username" required>
            </div>
            <div class="mb-4">
                <label class="form-label"><i class="fas fa-lock me-2"></i>Password</label>
                <input type="password" class="form-control" name="password" placeholder="Enter your password" required>
            </div>
            <button type="submit" class="btn btn-login"><i class="fas fa-sign-in-alt me-2"></i>Sign In</button>
        </form>
        
        <div class="text-center mt-4">
            <span style="color: #7f8c8d;">Don't have an account?</span> <a href="/user/register">Register</a>
        </div>
        <div class="text-center mt-3"><a href="/" style="color: #7f8c8d;"><i class="fas fa-arrow-left me-1"></i>Back to Home</a></div>
    </div>
</body>
</html>
