# Setup Guide - AI-Powered DSA Learning Portal

This guide will help you set up and run the AI-Powered DSA Learning Portal on your local machine.

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17 or higher** - [Download here](https://adoptium.net/)
- **Node.js 18 or higher** - [Download here](https://nodejs.org/)
- **Docker and Docker Compose** - [Download here](https://www.docker.com/products/docker-desktop/)
- **Git** - [Download here](https://git-scm.com/)
- **PostgreSQL** (optional, if not using Docker) - [Download here](https://www.postgresql.org/download/)

## 🚀 Quick Setup (Docker - Recommended)

### Step 1: Clone the Repository
```bash
git clone <repository-url>
cd ai-dsa-portal
```

### Step 2: Set Up Environment Variables
```bash
# Copy the example environment file
cp env.example .env

# Edit the .env file with your API keys
nano .env  # or use your preferred editor
```

### Step 3: Get API Keys

#### Google Gemini API Key
1. Visit [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Sign in with your Google account
3. Click "Create API Key"
4. Copy the API key and add it to your `.env` file

#### Judge0 API Key
1. Visit [RapidAPI Judge0](https://rapidapi.com/judge0-official/api/judge0-ce)
2. Sign up for a free account
3. Subscribe to the Judge0 CE API (free tier available)
4. Copy the API key and add it to your `.env` file

### Step 4: Start the Application
```bash
# Start all services
docker-compose up -d

# Check if all services are running
docker-compose ps
```

### Step 5: Access the Application
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **Database**: localhost:5432

## 🛠 Manual Setup (Local Development)

### Backend Setup

#### Step 1: Database Setup
```bash
# Start PostgreSQL (if using Docker)
docker run --name postgres-dsa -e POSTGRES_PASSWORD=password -e POSTGRES_DB=dsa_portal -p 5432:5432 -d postgres:15

# Or install PostgreSQL locally and create database
createdb dsa_portal
```

#### Step 2: Configure Backend
```bash
cd backend

# Update application.yml with your database credentials
# Edit src/main/resources/application.yml
```

#### Step 3: Run Backend
```bash
# Install dependencies and run
mvn clean install
mvn spring-boot:run
```

### Frontend Setup

#### Step 1: Install Dependencies
```bash
cd frontend
npm install
```

#### Step 2: Configure API URL
```bash
# Create .env file in frontend directory
echo "REACT_APP_API_URL=http://localhost:8080/api" > .env
```

#### Step 3: Run Frontend
```bash
npm start
```

## 🔧 Configuration Details

### Backend Configuration (`backend/src/main/resources/application.yml`)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/dsa_portal
    username: postgres
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: mySecretKey123456789012345678901234567890
  expiration: 86400000

gemini:
  api-key: ${GEMINI_API_KEY:your-gemini-api-key-here}
  base-url: https://generativelanguage.googleapis.com/v1beta

judge0:
  base-url: https://judge0-ce.p.rapidapi.com
  api-key: ${JUDGE0_API_KEY:your-judge0-api-key-here}
  host: judge0-ce.p.rapidapi.com
```

### Frontend Configuration (`frontend/.env`)

```env
REACT_APP_API_URL=http://localhost:8080/api
```

## 🗄️ Database Setup

### Using Docker (Recommended)
The database will be automatically set up when you run `docker-compose up -d`.

### Manual Database Setup
```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create database
CREATE DATABASE dsa_portal;

-- Connect to the database
\c dsa_portal;

-- Run the init script
\i database/init.sql
```

## 🧪 Testing the Setup

### Test Backend
```bash
# Test if backend is running
curl http://localhost:8080/api/problems

# Should return a JSON response with problems
```

### Test Frontend
1. Open http://localhost:3000 in your browser
2. You should see the login page
3. Try registering a new account

### Test Database
```bash
# Connect to database
psql -U postgres -d dsa_portal

# Check if tables exist
\dt

# Check sample data
SELECT * FROM problems LIMIT 5;
```

## 🔍 Troubleshooting

### Common Issues

#### 1. Port Already in Use
```bash
# Check what's using the port
lsof -i :8080  # for backend
lsof -i :3000  # for frontend
lsof -i :5432  # for database

# Kill the process
kill -9 <PID>
```

#### 2. Database Connection Issues
```bash
# Check if PostgreSQL is running
docker ps | grep postgres

# Check database logs
docker logs postgres-dsa
```

#### 3. API Key Issues
- Verify your API keys are correct
- Check if you have sufficient API quota
- Ensure the keys are properly set in the `.env` file

#### 4. Frontend Build Issues
```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

#### 5. Backend Build Issues
```bash
# Clean Maven cache
mvn clean

# Update dependencies
mvn dependency:resolve
```

### Logs and Debugging

#### View Docker Logs
```bash
# View all logs
docker-compose logs

# View specific service logs
docker-compose logs backend
docker-compose logs frontend
docker-compose logs postgres
```

#### Backend Logs
```bash
# If running locally
tail -f logs/application.log
```

#### Frontend Logs
```bash
# Check browser console for errors
# Open Developer Tools (F12) and check Console tab
```

## 🚀 Production Deployment

### Environment Variables for Production
```env
# Production database
POSTGRES_DB=dsa_portal_prod
POSTGRES_USER=dsa_user
POSTGRES_PASSWORD=secure_password

# Production JWT secret (generate a secure one)
JWT_SECRET=your_very_secure_jwt_secret_here

# Production API URLs
REACT_APP_API_URL=https://your-api-domain.com/api
```

### Docker Production Setup
```bash
# Build production images
docker-compose -f docker-compose.prod.yml build

# Deploy
docker-compose -f docker-compose.prod.yml up -d
```

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://reactjs.org/docs)
- [Docker Documentation](https://docs.docker.com/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [TailwindCSS Documentation](https://tailwindcss.com/docs)

## 🆘 Getting Help

If you encounter any issues:

1. Check the troubleshooting section above
2. Search existing issues in the repository
3. Create a new issue with detailed information about your problem
4. Include:
   - Operating system
   - Java version
   - Node.js version
   - Docker version
   - Error messages
   - Steps to reproduce

## ✅ Verification Checklist

- [ ] Java 17+ installed
- [ ] Node.js 18+ installed
- [ ] Docker and Docker Compose installed
- [ ] Repository cloned
- [ ] Environment variables configured
- [ ] API keys obtained and configured
- [ ] Application started successfully
- [ ] Frontend accessible at http://localhost:3000
- [ ] Backend API accessible at http://localhost:8080/api
- [ ] Database connected and initialized
- [ ] Can register/login
- [ ] Can view problems
- [ ] Can submit code
- [ ] AI recommendations working

---
