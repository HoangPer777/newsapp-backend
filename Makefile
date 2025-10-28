# News App Backend - Makefile
# Common commands for development and deployment

.PHONY: help build run test clean docker-up docker-down docker-logs docker-build

# Default target
help:
	@echo "News App Backend - Available Commands:"
	@echo ""
	@echo "Development:"
	@echo "  build          - Build the Spring Boot application"
	@echo "  run            - Run the Spring Boot application"
	@echo "  test           - Run tests"
	@echo "  clean          - Clean build artifacts"
	@echo ""
	@echo "Docker:"
	@echo "  docker-up      - Start all services with Docker Compose"
	@echo "  docker-down    - Stop all services"
	@echo "  docker-logs    - View logs from all services"
	@echo "  docker-build   - Build Docker image"
	@echo "  docker-ps      - Show running containers"
	@echo ""
	@echo "Database:"
	@echo "  db-up          - Start only database"
	@echo "  db-reset       - Reset database (WARNING: deletes all data)"
	@echo ""
	@echo "Utilities:"
	@echo "  install        - Install dependencies"
	@echo "  format         - Format code"
	@echo "  check          - Run code quality checks"

# Development commands
build:
	@echo "Building Spring Boot application..."
	cd newsapp && mvn clean package -DskipTests

run:
	@echo "Running Spring Boot application..."
	cd newsapp && mvn spring-boot:run

test:
	@echo "Running tests..."
	cd newsapp && mvn test

clean:
	@echo "Cleaning build artifacts..."
	cd newsapp && mvn clean
	docker system prune -f

# Docker commands
docker-up:
	@echo "Starting all services with Docker Compose..."
	docker compose up -d

docker-down:
	@echo "Stopping all services..."
	docker compose down

docker-logs:
	@echo "Viewing logs from all services..."
	docker compose logs -f

docker-build:
	@echo "Building Docker image..."
	docker compose build

docker-ps:
	@echo "Showing running containers..."
	docker compose ps

# Database commands
db-up:
	@echo "Starting database only..."
	docker compose up db -d

db-reset:
	@echo "WARNING: This will delete all data!"
	@read -p "Are you sure? (y/N): " confirm && [ "$$confirm" = "y" ]
	docker compose down -v
	docker compose up db -d

# Utility commands
install:
	@echo "Installing dependencies..."
	cd newsapp && mvn clean install

format:
	@echo "Formatting code..."
	cd newsapp && mvn spotless:apply

check:
	@echo "Running code quality checks..."
	cd newsapp && mvn checkstyle:check
	cd newsapp && mvn spotbugs:check

# Quick start
quick-start: build docker-up
	@echo "Quick start completed!"
	@echo "API: http://localhost:8080"
	@echo "pgAdmin: http://localhost:5050"

# Full setup for new developers
setup: install docker-up
	@echo "Development environment setup completed!"
	@echo "API: http://localhost:8080"
	@echo "pgAdmin: http://localhost:5050"
	@echo "Database: localhost:5432"

