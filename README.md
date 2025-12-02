# AI-Powered DSA Learning Portal

A comprehensive full-stack web application that helps students practice Data Structures & Algorithms (DSA) problems with AI-powered recommendations using Google Gemini API and code execution via Judge0 API.

## 🚀 Features

### Core Features
- **User Authentication**: JWT-based login and registration system
- **Problem Database**: Comprehensive collection of DSA problems with multiple difficulty levels
- **Code Editor**: Integrated Monaco Editor supporting Python, Java, C++, JavaScript, and C
- **Code Execution**: Real-time code execution using Judge0 API
- **AI Recommendations**: Personalized problem suggestions using Google Gemini AI
- **Analytics Dashboard**: Visual insights into user performance and progress
- **Submission Tracking**: Complete history of user submissions and results

### AI-Powered Features
- **Smart Recommendations**: AI analyzes user performance to suggest relevant problems
- **Performance Analysis**: Tracks accuracy by topic and difficulty
- **Personalized Learning Path**: Adapts recommendations based on weak areas

## 🛠 Tech Stack

### Backend
- **Spring Boot 3.2.0** - Java framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **PostgreSQL** - Primary database
- **JWT** - Token-based authentication
- **WebClient** - HTTP client for external APIs

### Frontend
- **React 18** - UI framework
- **React Router** - Client-side routing
- **TailwindCSS** - Styling framework
- **Monaco Editor** - Code editor
- **Recharts** - Data visualization
- **Axios** - HTTP client

### External APIs
- **Google Gemini API** - AI recommendations
- **Judge0 API** - Code execution

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

## 📁 Project Structure

```
ai-dsa-portal/
├── backend/                    # Spring Boot backend
│   ├── src/main/java/com/dsaportal/
│   │   ├── entity/            # JPA entities
│   │   ├── repository/        # Data repositories
│   │   ├── service/           # Business logic
│   │   ├── controller/        # REST controllers
│   │   ├── dto/              # Data transfer objects
│   │   ├── security/         # Security configuration
│   │   └── AiDsaPortalApplication.java
│   ├── src/main/resources/
│   │   └── application.yml    # Configuration
│   └── pom.xml               # Maven dependencies
├── frontend/                  # React frontend
│   ├── src/
│   │   ├── components/       # Reusable components
│   │   ├── pages/           # Page components
│   │   ├── contexts/        # React contexts
│   │   ├── services/        # API services
│   │   └── App.js
│   ├── public/
│   └── package.json
├── database/
│   └── init.sql             # Database schema
├── docker-compose.yml       # Docker orchestration
├── env.example             # Environment variables template
└── README.md

3. **Configure application.yml**
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/dsa_portal
       username: your_username
       password: your_password
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

#### Frontend Setup
1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   ```

## 🔧 Configuration

### Environment Variables

Create a `.env` file in the root directory:

```env
# Gemini AI API Configuration
GEMINI_API_KEY=your-gemini-api-key-here

# Judge0 API Configuration
JUDGE0_API_KEY=your-judge0-api-key-here

# Database Configuration
POSTGRES_DB=dsa_portal
POSTGRES_USER=postgres
POSTGRES_PASSWORD=password

# JWT Configuration
JWT_SECRET=mySecretKey123456789012345678901234567890
JWT_EXPIRATION=86400000
```

### API Keys Setup

1. **Google Gemini API**
   - Visit [Google AI Studio](https://makersuite.google.com/app/apikey)
   - Create a new API key
   - Add it to your `.env` file

2. **Judge0 API**
   - Visit [RapidAPI Judge0](https://rapidapi.com/judge0-official/api/judge0-ce)
   - Subscribe to the API
   - Get your API key
   - Add it to your `.env` file

## 📊 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Problems
- `GET /api/problems` - Get all problems (with filters)
- `GET /api/problems/{id}` - Get problem by ID
- `POST /api/problems` - Create problem (Admin only)
- `PUT /api/problems/{id}` - Update problem (Admin only)
- `DELETE /api/problems/{id}` - Delete problem (Admin only)

### Submissions
- `GET /api/submissions/user/{userId}` - Get user submissions
- `GET /api/submissions/problem/{problemId}` - Get problem submissions
- `POST /api/submissions` - Submit code
- `PUT /api/submissions/{id}/result` - Update submission result

### Dashboard
- `GET /api/dashboard/{userId}` - Get dashboard statistics
- `GET /api/dashboard/recommendations/{userId}` - Get AI recommendations

## 🎯 Usage

### For Students
1. **Register/Login** to create an account
2. **Browse Problems** by difficulty, topic, or search
3. **Solve Problems** using the integrated code editor
4. **Submit Solutions** and get real-time feedback
5. **View Analytics** on the dashboard
6. **Get AI Recommendations** for personalized learning

### For Administrators
1. **Manage Problems** - Add, edit, or delete problems
2. **Monitor Submissions** - View all user submissions
3. **Analytics** - Track platform usage and performance

## 🐳 Running with Docker (Unified Image)

The entire application (Frontend + Backend + Database) now runs in a **single container**.

1.  **Build the Image**:
    ```bash
    docker build -t dsa-portal-unified .
    ```

2.  **Run the Container** (Two Options):

    ### Option A: With Data Persistence (Recommended)
    This saves your database data even after container removal:
    ```bash
    docker run -d \
      --name dsa-portal \
      -p 3000:3000 \
      -p 8080:8080 \
      -v dsa-portal-data:/var/lib/postgresql/14/main \
      dsa-portal-unified
    ```

    ### Option B: Without Persistence (Fresh Start)
    ```bash
    docker run -p 3000:3000 -p 8080:8080 dsa-portal-unified
    ```

3.  **Access the Application**:
    -   Frontend: `http://localhost:3000`
    -   Backend API: `http://localhost:8080`

## 🔄 Managing Your Container

```bash
# Stop the container
docker stop dsa-portal

# Start it again (data is preserved if using volumes)
docker start dsa-portal

# View logs
docker logs dsa-portal

# Remove the container
docker rm dsa-portal

# Remove the container AND data volume
docker rm dsa-portal
docker volume rm dsa-portal-data
```

## 💻 Running Locally (Alternative)

If you prefer running components separately without Docker:

### 1. Database Setup
-   Ensure PostgreSQL is running.
-   Create a database named `dsa_portal`.
-   Update `.env` with your database credentials.

### 2. Backend Setup
```bash
cd backend
mvn spring-boot:run
```

### 3. Frontend Setup
```bash
cd frontend
npm install
npm start
```

## 🧪 Default Credentials

-   **Admin User**: `admin` / `admin123`
-   **Test User**: `testuser` / `password123`

## 📝 License

This project is licensed under the MIT License.

## 🔒 Security Features

- **JWT Authentication** - Secure token-based authentication
- **Password Encryption** - BCrypt password hashing
- **CORS Configuration** - Cross-origin resource sharing
- **Input Validation** - Server-side validation for all inputs
- **SQL Injection Protection** - JPA/Hibernate ORM protection

## 📈 Performance Features

- **Database Indexing** - Optimized database queries
- **Lazy Loading** - Efficient data loading
- **Caching** - Response caching for better performance
- **Pagination** - Efficient data pagination
- **Async Processing** - Non-blocking code execution

## 🧪 Testing

### Backend Testing
```bash
cd backend
mvn test
```

### Frontend Testing
```bash
cd frontend
npm test
```

## 🚀 Deployment

### Production Deployment

1. **Build the application**
   ```bash
   # Backend
   cd backend
   mvn clean package -DskipTests
   
   # Frontend
   cd frontend
   npm run build
   ```

2. **Deploy with Docker**
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

### Cloud Deployment

#### Frontend (Vercel)
1. Connect your GitHub repository to Vercel
2. Set environment variables
3. Deploy automatically

#### Backend (AWS/GCP)
1. Build Docker image
2. Push to container registry
3. Deploy to cloud service (ECS, GKE, etc.)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

