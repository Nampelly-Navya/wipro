# Music Library Microservices

A complete microservices-based Music Library application built with Spring Boot 3.x and Spring Cloud.

## Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                        MUSIC LIBRARY SYSTEM                          │
├─────────────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │   EUREKA    │  │   CONFIG    │  │     API     │  │    WEB      │ │
│  │   SERVER    │  │   SERVER    │  │   GATEWAY   │  │    APP      │ │
│  │   (8761)    │  │   (8888)    │  │   (8080)    │  │   (9090)    │ │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘ │
│                                           │                          │
│  ┌────────────────────────────────────────┼────────────────────────┐ │
│  │                    MICROSERVICES       │                        │ │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐              │ │
│  │  │    ADMIN    │  │    USER     │  │ NOTIFICATION│              │ │
│  │  │   SERVICE   │  │   SERVICE   │  │   SERVICE   │              │ │
│  │  │   (8081)    │  │   (8082)    │  │   (8083)    │              │ │
│  │  └──────┬──────┘  └──────┬──────┘  └─────────────┘              │ │
│  │         │                │                                       │ │
│  │         ▼                ▼                                       │ │
│  │  ┌─────────────┐  ┌─────────────┐                               │ │
│  │  │   MySQL     │  │   MySQL     │                               │ │
│  │  │ admin_db    │  │  user_db    │                               │ │
│  │  └─────────────┘  └─────────────┘                               │ │
│  └─────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| Eureka Server | 8761 | Service Discovery |
| Config Server | 8888 | Centralized Configuration |
| API Gateway | 8080 | Single Entry Point |
| Admin Service | 8081 | Song Management |
| User Service | 8082 | Auth, Playlists, Player |
| Notification Service | 8083 | Notifications |
| Web Application | 9090 | JSP Frontend |

## Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.0+

## Database Setup

```sql
CREATE DATABASE admin_service;
CREATE DATABASE user_service;
CREATE DATABASE notification_service;
```

## Quick Start

```bash
cd scripts
start-all.bat
```

## Access URLs

| URL | Description |
|-----|-------------|
| http://localhost:9090 | Web Application Home |
| http://localhost:9090/admin | Admin Panel |
| http://localhost:9090/user/login | User Login |
| http://localhost:8080 | API Gateway |
| http://localhost:8761 | Eureka Dashboard |
