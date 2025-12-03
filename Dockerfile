# Stage 1: Build Backend
FROM eclipse-temurin:21-jdk AS backend-builder
WORKDIR /build
COPY backend/pom.xml backend/src ./
COPY backend/pom.xml .
COPY backend/src ./src
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Stage 2: Build Frontend
FROM node:18-alpine AS frontend-builder
WORKDIR /build
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Stage 3: Runtime
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# Install runtime dependencies
RUN apt-get update && apt-get install -y \
    openjdk-21-jre \
    postgresql-14 \
    postgresql-contrib-14 \
    curl \
    dos2unix \
    && rm -rf /var/lib/apt/lists/*

# Install Node.js and serve
RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g serve

# Setup PostgreSQL
RUN service postgresql start && \
    su postgres -c "psql -c \"ALTER USER postgres PASSWORD '00000';\"" && \
    su postgres -c "psql -c \"CREATE DATABASE dsa_portal;\"" && \
    service postgresql stop

# Copy built artifacts
WORKDIR /app
COPY --from=backend-builder /build/target/*.jar /app/backend.jar
COPY --from=frontend-builder /build/build /app/frontend
COPY database/init.sql /app/init.sql
COPY database/problems_data.sql /app/problems_data.sql
COPY entrypoint.sh /app/entrypoint.sh

RUN dos2unix /app/entrypoint.sh && chmod +x /app/entrypoint.sh

# Environment variables
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/dsa_portal
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=00000

EXPOSE 3000 8080

CMD ["/app/entrypoint.sh"]
