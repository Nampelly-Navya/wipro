@echo off
echo ===========================================
echo    MUSIC LIBRARY MICROSERVICES STARTUP
echo ===========================================
echo.

echo [1/7] Starting Eureka Server (port 8761)...
cd /d "%~dp0\..\eureka-server"
start "Eureka Server" cmd /k "mvn spring-boot:run"
timeout /t 20 /nobreak > nul

echo [2/7] Starting Config Server (port 8888)...
cd /d "%~dp0\..\config-server"
start "Config Server" cmd /k "mvn spring-boot:run"
timeout /t 15 /nobreak > nul

echo [3/7] Starting API Gateway (port 8080)...
cd /d "%~dp0\..\api-gateway"
start "API Gateway" cmd /k "mvn spring-boot:run"
timeout /t 10 /nobreak > nul

echo [4/7] Starting Admin Service (port 8081)...
cd /d "%~dp0\..\admin-service"
start "Admin Service" cmd /k "mvn spring-boot:run"
timeout /t 10 /nobreak > nul

echo [5/7] Starting User Service (port 8082)...
cd /d "%~dp0\..\user-service"
start "User Service" cmd /k "mvn spring-boot:run"
timeout /t 10 /nobreak > nul

echo [6/7] Starting Notification Service (port 8083)...
cd /d "%~dp0\..\notification-service"
start "Notification Service" cmd /k "mvn spring-boot:run"
timeout /t 10 /nobreak > nul

echo [7/7] Starting Web Application (port 9090)...
cd /d "%~dp0\..\web-app"
start "Web App" cmd /k "mvn spring-boot:run"
timeout /t 10 /nobreak > nul

echo.
echo ===========================================
echo    ALL SERVICES STARTED!
echo ===========================================
echo.
echo URLs:
echo   Home Page:    http://localhost:9090
echo   Admin Panel:  http://localhost:9090/admin
echo   User Login:   http://localhost:9090/user/login
echo   API Gateway:  http://localhost:8080
echo   Eureka:       http://localhost:8761
echo.
pause
