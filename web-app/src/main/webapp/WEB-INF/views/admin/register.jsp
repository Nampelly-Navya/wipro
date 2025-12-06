<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Registration - Music Library</title>
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webjars/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Poppins', sans-serif; }
        body { background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%); min-height: 100vh; padding: 40px 0; }
        .register-card { background: white; border-radius: 20px; padding: 50px; max-width: 550px; margin: 0 auto; box-shadow: 0 20px 60px rgba(0,0,0,0.1); border: 1px solid #eee; }
        .form-control { background: #f8f9fa; border: 2px solid #e9ecef; color: #2c3e50; padding: 12px 15px; border-radius: 10px; }
        .form-control:focus { background: white; color: #2c3e50; border-color: #e74c3c; box-shadow: 0 0 0 3px rgba(231,76,60,0.1); }
        .form-label { color: #2c3e50; font-weight: 500; }
        .btn-register { background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%); border: none; padding: 15px; width: 100%; border-radius: 10px; font-weight: 600; font-size: 1.1rem; color: white; }
        .btn-register:hover { background: linear-gradient(135deg, #c0392b 0%, #a93226 100%); color: white; }
        .info-box { background: #fef5f5; border-left: 4px solid #e74c3c; padding: 15px; border-radius: 0 10px 10px 0; margin-bottom: 25px; color: #c0392b; }
        h2 { color: #2c3e50; font-weight: 600; }
        a { color: #e74c3c; text-decoration: none; font-weight: 500; }
        a:hover { color: #c0392b; }
    </style>
</head>
<body>
    <div class="register-card">
        <div class="text-center mb-4">
            <i class="fas fa-user-plus fa-3x mb-3" style="color: #e74c3c;"></i>
            <h2>Create Admin Account</h2>
        </div>
        
        <div class="info-box">
            <i class="fas fa-envelope me-2"></i>A welcome email will be sent upon registration!
        </div>
        
        <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
        
        <form action="/admin/register" method="post">
            <div class="mb-3">
                <label class="form-label"><i class="fas fa-id-card me-2"></i>Full Name *</label>
                <input type="text" class="form-control" name="adminName" placeholder="Enter your full name" required>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label"><i class="fas fa-user me-2"></i>Username *</label>
                    <input type="text" class="form-control" name="username" placeholder="Choose username" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label"><i class="fas fa-lock me-2"></i>Password *</label>
                    <input type="password" class="form-control" name="password" placeholder="Create password" required>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label"><i class="fas fa-envelope me-2"></i>Email *</label>
                    <input type="email" class="form-control" name="email" placeholder="admin@example.com" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label"><i class="fas fa-phone me-2"></i>Mobile *</label>
                    <input type="text" class="form-control" name="mobile" placeholder="Phone number" required>
                </div>
            </div>
            <button type="submit" class="btn btn-register"><i class="fas fa-user-plus me-2"></i>Create Admin Account</button>
        </form>
        
        <div class="text-center mt-4">
            <span style="color: #7f8c8d;">Already have an account?</span> <a href="/admin/login">Sign In</a>
        </div>
        <div class="text-center mt-2">
            <a href="/" style="color: #7f8c8d;"><i class="fas fa-arrow-left me-1"></i>Back to Home</a>
        </div>
    </div>
</body>
</html>
