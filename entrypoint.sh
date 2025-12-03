#!/bin/bash
set -e

echo "=== Starting AI-Powered DSA Learning Portal ==="

# Start PostgreSQL
echo "[1/4] Starting PostgreSQL..."
service postgresql start
sleep 3

# Initialize database if needed
echo "[2/4] Initializing database..."
su postgres -c "psql -lqt | cut -d \| -f 1 | grep -qw dsa_portal" || \
su postgres -c "psql -c \"CREATE DATABASE dsa_portal;\""
su postgres -c "psql -d dsa_portal -f /app/init.sql" 2>/dev/null || echo "Database already initialized"
su postgres -c "psql -d dsa_portal -f /app/problems_data.sql" 2>/dev/null || echo "Problems data loaded"

# Start Backend
echo "[3/4] Starting Spring Boot Backend..."
cd /app
java -jar backend.jar &
BACKEND_PID=$!

# Wait for backend to start
sleep 10

# Start Frontend
echo "[4/4] Starting React Frontend..."
cd /app/frontend
serve -s . -l 3000 &
FRONTEND_PID=$!

echo ""
echo "===================================="
echo "✅ All services started successfully!"
echo "===================================="
echo "Frontend: http://localhost:3000"
echo "Backend:  http://localhost:8080"
echo "===================================="

# Keep container running
wait $BACKEND_PID $FRONTEND_PID
