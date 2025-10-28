# Use OpenJDK 17 as base image
# Use Java 21 to match the compiled JAR (class file version 65)
FROM eclipse-temurin:21-jdk-jammy

# Install curl for container healthcheck
RUN apt-get update \
  && apt-get install -y --no-install-recommends curl \
  && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY newsapp/target/newsapp-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=postgres
ENV SERVER_PORT=8080

# Health check (give app time to warm up)
HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=5 \
  CMD curl -fsS http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
