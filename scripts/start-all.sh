#!/bin/bash

echo "==========================================="
echo "   MUSIC LIBRARY MICROSERVICES STARTUP"
echo "==========================================="
echo

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
BASE_DIR="$SCRIPT_DIR/.."

echo "[1/7] Starting Eureka Server (port 8761)..."
cd "$BASE_DIR/eureka-server" && mvn spring-boot:run &
sleep 20

echo "[2/7] Starting Config Server (port 8888)..."
cd "$BASE_DIR/config-server" && mvn spring-boot:run &
sleep 15

echo "[3/7] Starting API Gateway (port 8080)..."
cd "$BASE_DIR/api-gateway" && mvn spring-boot:run &
sleep 10

echo "[4/7] Starting Admin Service (port 8081)..."
cd "$BASE_DIR/admin-service" && mvn spring-boot:run &
sleep 10

echo "[5/7] Starting User Service (port 8082)..."
cd "$BASE_DIR/user-service" && mvn spring-boot:run &
sleep 10

echo "[6/7] Starting Notification Service (port 8083)..."
cd "$BASE_DIR/notification-service" && mvn spring-boot:run &
sleep 10

echo "[7/7] Starting Web Application (port 9090)..."
cd "$BASE_DIR/web-app" && mvn spring-boot:run &
sleep 10

echo
echo "==========================================="
echo "   ALL SERVICES STARTED!"
echo "==========================================="
echo
echo "URLs:"
echo "  Home Page:    http://localhost:9090"
echo "  Admin Panel:  http://localhost:9090/admin"
echo "  User Login:   http://localhost:9090/user/login"
echo "  API Gateway:  http://localhost:8080"
echo "  Eureka:       http://localhost:8761"
echo
