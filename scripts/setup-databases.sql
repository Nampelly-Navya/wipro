-- Music Library Database Setup
-- Run this script in MySQL to create all required databases

CREATE DATABASE IF NOT EXISTS admin_service;
CREATE DATABASE IF NOT EXISTS user_service;
CREATE DATABASE IF NOT EXISTS notification_service;

-- Verify databases created
SHOW DATABASES;

-- Note: Tables will be auto-created by Spring JPA with ddl-auto=update
