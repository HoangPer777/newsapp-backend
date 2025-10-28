@echo off
echo ========================================
echo    News App Backend - Quick Start
echo ========================================
echo.

echo [1/4] Checking Docker...
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker is not installed or not running!
    echo Please install Docker Desktop and try again.
    pause
    exit /b 1
)

echo [2/4] Building Spring Boot application...
cd newsapp
call mvn clean package -DskipTests -q
if %errorlevel% neq 0 (
    echo ERROR: Failed to build Spring Boot application!
    pause
    exit /b 1
)
cd ..

echo [3/4] Starting Docker services...
docker compose up -d

echo [4/4] Checking services...
timeout /t 5 /nobreak >nul
docker compose ps

echo.
echo ========================================
echo    Services are ready!
echo ========================================
echo.
echo Spring Boot API: http://localhost:8080
echo pgAdmin:         http://localhost:5050
echo                  Email: admin@example.com
echo                  Password: admin
echo.
echo Press any key to view logs...
pause >nul
docker compose logs -f
