# How to Run the AI-Powered DSA Learning Portal

## 🚀 Quick Start (Unified Docker Image)

This project runs as a **single Docker container** containing the frontend, backend, and database.

### Prerequisites
- Docker Desktop installed and running
- Ports 3000 and 8080 available

---

## ✅ Step-by-Step Instructions

### 1. Build the Docker Image

```bash
docker build -t dsa-portal-unified .
```

**This command:**
- Builds the backend (Spring Boot)
- Builds the frontend (React)
- Sets up PostgreSQL database
- Creates a single unified image

**Expected time:** 2-5 minutes (first build)

---

### 2. Run the Container

**Option A: With Data Persistence (Recommended)**

This saves your database data permanently:

```bash
docker run -d \
  --name dsa-portal \
  -p 3000:3000 \
  -p 8080:8080 \
  -v dsa-portal-data:/var/lib/postgresql/14/main \
  dsa-portal-unified
```

**Option B: Without Data Persistence (Fresh Start)**

Data will be lost when container is removed:

```bash
docker run -d \
  --name dsa-portal \
  -p 3000:3000 \
  -p 8080:8080 \
  dsa-portal-unified
```

**On Windows PowerShell, use this format:**
```powershell
docker run -d --name dsa-portal -p 3000:3000 -p 8080:8080 -v dsa-portal-data:/var/lib/postgresql/14/main dsa-portal-unified
```

---

### 3. Wait for Startup

The application takes about 15-20 seconds to fully start. You can check the logs:

```bash
docker logs dsa-portal -f
```

**Look for these messages:**
- `[1/4] Starting PostgreSQL...`
- `[2/4] Initializing database...`
- `[3/4] Starting Spring Boot Backend...`
- `[4/4] Starting React Frontend...`
- `✅ All services started successfully!`

Press `Ctrl+C` to stop watching logs.

---

### 4. Access the Application

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api

---

## 🔐 Default Login Credentials

**Admin Account:**
- Username: `admin`
- Password: `admin123`

**Test User Account:**
- Username: `testuser`
- Password: `password123`

---

## 🔄 Container Management

### Check if Container is Running
```bash
docker ps
```

### View Container Logs
```bash
docker logs dsa-portal
```

### Stop the Container
```bash
docker stop dsa-portal
```

### Start the Container Again
```bash
docker start dsa-portal
```
*If you used volumes, all your data will be preserved!*

### Restart the Container
```bash
docker restart dsa-portal
```

### Remove the Container
```bash
docker rm dsa-portal
```

### Remove Container AND Data Volume
```bash
docker rm dsa-portal
docker volume rm dsa-portal-data
```

---

## 🔧 Troubleshooting

### Container Won't Start
```bash
# Check if ports are already in use
docker ps -a

# Remove old containers
docker rm -f dsa-portal

# Try running again
docker run -d --name dsa-portal -p 3000:3000 -p 8080:8080 dsa-portal-unified
```

### Login Not Working
- Make sure you're using the correct credentials:
  - `admin` / `admin123`
  - `testuser` / `password123`
- Wait 20 seconds after starting for database to initialize
- Check logs: `docker logs dsa-portal`

### Port Already in Use
```bash
# On Windows, find what's using the port:
netstat -ano | findstr :3000
netstat -ano | findstr :8080

# Then kill the process or use different ports:
docker run -d --name dsa-portal -p 3001:3000 -p 8081:8080 dsa-portal-unified
```

### Database Errors
```bash
# Remove everything and start fresh:
docker stop dsa-portal
docker rm dsa-portal
docker volume rm dsa-portal-data
docker build -t dsa-portal-unified .
docker run -d --name dsa-portal -p 3000:3000 -p 8080:8080 -v dsa-portal-data:/var/lib/postgresql/14/main dsa-portal-unified
```

---

## 🔄 Rebuilding After Code Changes

If you make changes to the code:

```bash
# Stop and remove the current container
docker stop dsa-portal
docker rm dsa-portal

# Rebuild the image
docker build -t dsa-portal-unified .

# Run the new container
docker run -d --name dsa-portal -p 3000:3000 -p 8080:8080 -v dsa-portal-data:/var/lib/postgresql/14/main dsa-portal-unified
```

**Note:** The database volume (`dsa-portal-data`) is preserved, so your data remains intact!

---

## 📊 What's Running Inside the Container?

1. **PostgreSQL Database** (Port 5432 internal)
   - Stores users, problems, submissions
   - Sample data pre-loaded

2. **Spring Boot Backend** (Port 8080)
   - REST API
   - JWT Authentication
   - Judge0 & Gemini AI integration

3. **React Frontend** (Port 3000)
   - User Interface
   - Code Editor
   - Dashboard & Analytics

---

## ❌ Common Mistakes to Avoid

- ❌ Don't use `docker-compose` (we're using single unified image now)
- ❌ Don't forget to wait 15-20 seconds for startup
- ❌ Don't use old credentials (`admin`/`admin` won't work, use `admin123`)
- ❌ Don't remove volumes if you want to keep your data

---

## 💡 Pro Tips

1. **Always use named containers** (`--name dsa-portal`) for easy management
2. **Use volumes** (`-v dsa-portal-data:...`) to persist data
3. **Run in detached mode** (`-d`) so container runs in background
4. **Check logs** when troubleshooting with `docker logs dsa-portal`
5. **Backup your volume** before major changes

---

## 🎯 Quick Reference

```bash
# Complete workflow
docker build -t dsa-portal-unified .
docker run -d --name dsa-portal -p 3000:3000 -p 8080:8080 -v dsa-portal-data:/var/lib/postgresql/14/main dsa-portal-unified
docker logs dsa-portal -f

# Stop/Start
docker stop dsa-portal
docker start dsa-portal

# Clean up everything
docker stop dsa-portal && docker rm dsa-portal && docker volume rm dsa-portal-data
```

---

## 📞 Need Help?

If you encounter any issues:
1. Check the logs: `docker logs dsa-portal`
2. Verify Docker is running: `docker ps`
3. Check ports are free: `netstat -ano | findstr :3000`
4. Try a fresh rebuild (see "Database Errors" section above)
