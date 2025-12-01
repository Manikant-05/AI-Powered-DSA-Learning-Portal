# How to Run This Project

## ⚠️ Important: This is NOT a Python Project

This is a **Spring Boot (Java) + React (Node.js)** project. Do NOT try to run it with Python/uvicorn.

## ✅ Correct Way to Run

### Using Docker Compose (Recommended)

1. **Make sure Docker is running**

2. **Start all services:**
   ```powershell
   docker-compose -f docker-compose.yml up -d
   ```

3. **Wait for services to start (30-40 seconds):**
   ```powershell
   docker-compose -f docker-compose.yml logs -f
   ```
   Press Ctrl+C to stop watching logs once you see "Started AiDsaPortalApplication"

4. **Access the application:**
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080/api

5. **Login credentials:**
   - Username: `admin` / Password: `admin`
   - OR Username: `testuser` / Password: `password123`

### Check if Services are Running

```powershell
docker-compose -f docker-compose.yml ps
```

You should see 3 services:
- `dsa_portal_postgres` (database)
- `dsa_portal_backend` (Spring Boot API)
- `dsa_portal_frontend` (React app)

### Stop Services

```powershell
docker-compose -f docker-compose.yml down
```

### Rebuild After Code Changes

```powershell
docker-compose -f docker-compose.yml up -d --build
```

## ❌ What NOT to Do

- ❌ Don't run `python` or `uvicorn` commands
- ❌ Don't use the root `Dockerfile` (it's for Python, not this project)
- ❌ Don't use `compose.debug.yaml` (it's for Python debugging)
- ❌ Don't install Python dependencies from `requirements.txt`

## 📁 Project Structure

- `backend/` - Spring Boot Java application
- `frontend/` - React JavaScript application
- `docker-compose.yml` - Main Docker configuration (USE THIS)
- `backend/Dockerfile` - Backend Docker image
- `frontend/Dockerfile` - Frontend Docker image

## 🐛 If You See Python Errors

If you see errors about Python/uvicorn, it means:
1. Your IDE might be trying to run it as Python
2. You might have clicked "Run" on a Python file
3. Check your IDE's run configurations and make sure you're using Docker Compose

## ✅ Always Use Docker Compose

This project is designed to run in Docker containers. Always use:
```powershell
docker-compose -f docker-compose.yml up -d
```

