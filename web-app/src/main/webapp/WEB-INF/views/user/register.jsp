<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - Music Library</title>
    <link href="/webjars/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="/webjars/font-awesome/6.4.2/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { font-family: 'Poppins', sans-serif; }
        body { background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%); min-height: 100vh; padding: 40px 0; }
        .register-card { background: white; border-radius: 20px; padding: 50px; max-width: 600px; margin: 0 auto; box-shadow: 0 20px 60px rgba(0,0,0,0.1); border: 1px solid #eee; }
        .form-control { background: #f8f9fa; border: 2px solid #e9ecef; color: #2c3e50; padding: 12px 15px; border-radius: 10px; }
        .form-control:focus { background: white; color: #2c3e50; border-color: #3498db; box-shadow: 0 0 0 3px rgba(52,152,219,0.1); }
        .form-label { color: #2c3e50; font-weight: 500; }
        .btn-register { background: linear-gradient(135deg, #3498db 0%, #2980b9 100%); border: none; padding: 15px; width: 100%; border-radius: 10px; font-weight: 600; color: white; }
        .btn-register:hover { background: linear-gradient(135deg, #2980b9 0%, #1f6dad 100%); color: white; }
        h3 { color: #2c3e50; font-weight: 600; }
        a { color: #3498db; text-decoration: none; font-weight: 500; }
        a:hover { color: #2980b9; }
    </style>
</head>
<body>
    <div class="register-card">
        <div class="text-center mb-4">
            <i class="fas fa-user-plus fa-3x" style="color: #3498db;"></i>
            <h3 class="mt-3">Create Account</h3>
        </div>
        
        <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
        
        <form action="/user/register" method="post">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">First Name *</label>
                    <input type="text" class="form-control" name="firstName" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Last Name *</label>
                    <input type="text" class="form-control" name="lastName" required>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Username *</label>
                    <input type="text" class="form-control" name="username" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Password *</label>
                    <input type="password" class="form-control" name="password" required>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Email *</label>
                    <input type="email" class="form-control" name="email" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Mobile *</label>
                    <input type="text" class="form-control" name="mobile" required>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label">Address</label>
                <input type="text" class="form-control" name="address1">
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Address 2</label>
                    <input type="text" class="form-control" name="address2">
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">City</label>
                    <input type="text" class="form-control" name="city">
                </div>
            </div>
            <button type="submit" class="btn btn-register"><i class="fas fa-user-plus me-2"></i>Create Account</button>
        </form>
        
        <div class="text-center mt-4">
            <span style="color: #7f8c8d;">Already have an account?</span> <a href="/user/login">Sign in</a>
        </div>
        <div class="text-center mt-2">
            <a href="/" style="color: #7f8c8d;"><i class="fas fa-arrow-left me-1"></i>Back to Home</a>
        </div>
    </div>
</body>
</html>
