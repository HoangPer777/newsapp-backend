#!/bin/bash

echo "========================================"
echo "   News App Backend - Quick Start"
echo "========================================"
echo

echo "[1/4] Checking Docker..."
if ! command -v docker &> /dev/null; then
    echo "ERROR: Docker is not installed or not running!"
    echo "Please install Docker Desktop and try again."
    exit 1
fi

echo "[2/4] Building Spring Boot application..."
cd newsapp
mvn clean package -DskipTests -q
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to build Spring Boot application!"
    exit 1
fi
cd ..

echo "[3/4] Starting Docker services..."
docker compose up -d

echo "[4/4] Checking services..."
sleep 5
docker compose ps

echo
echo "========================================"
echo "   Services are ready!"
echo "========================================"
echo
echo "Spring Boot API: http://localhost:8080"
echo "pgAdmin:         http://localhost:5050"
echo "                 Email: admin@example.com"
echo "                 Password: admin"
echo
echo "Press Enter to view logs..."
read
docker compose logs -f
