# Docker Setup Guide for Hospital Management System

## Prerequisites
- Docker Desktop installed and running
- Docker Compose (usually included with Docker Desktop)

## Quick Start

### 1. Build and Start Services
```bash
docker-compose up --build
```

This will:
- Build the Spring Boot application
- Start PostgreSQL database
- Start the Spring Boot application
- Create necessary volumes for data persistence

### 2. Check if Services are Running
```bash
docker-compose ps
```

You should see both `hospital-postgres` and `hospital-app` containers running.

### 3. View Logs
```bash
# View all logs
docker-compose logs -f

# View only application logs
docker-compose logs -f hospital-app

# View only database logs
docker-compose logs -f postgres
```

## Testing in Postman

### Base URL
```
http://localhost:8081
```

### Swagger UI
```
http://localhost:8081/swagger-ui/index.html
```

### Quick Test Flow

1. **Register a User:**
   - **Method:** POST
   - **URL:** `http://localhost:8081/auth/register`
   - **Body (JSON):**
   ```json
   {
     "username": "testuser",
     "email": "test@example.com",
     "password": "password123",
     "role": "PATIENT"
   }
   ```

2. **Login:**
   - **Method:** POST
   - **URL:** `http://localhost:8081/auth/login`
   - **Body (JSON):**
   ```json
   {
     "username": "testuser",
     "password": "password123"
   }
   ```
   - **Response:** Copy the `token` from the response

3. **Use Token in Subsequent Requests:**
   - Add header: `Authorization: Bearer {your-token-here}`

4. **Test Protected Endpoints:**
   - Create Appointment: `POST http://localhost:8081/appointments`
   - Get Appointments: `GET http://localhost:8081/appointments`
   - View Analytics: `GET http://localhost:8081/analytics/status-count`

## Common Commands

### Stop Services
```bash
docker-compose down
```

### Stop and Remove Volumes (Clean Start)
```bash
docker-compose down -v
```

### Rebuild After Code Changes
```bash
docker-compose up --build
```

### Run in Detached Mode (Background)
```bash
docker-compose up -d --build
```

### Access Database (Optional)
```bash
# Connect to PostgreSQL container
docker exec -it hospital-postgres psql -U hospitaluser -d hospitaldb

# Or use any PostgreSQL client with:
# Host: localhost
# Port: 5432
# Database: hospitaldb
# Username: hospitaluser
# Password: hospitalpass
```

## Environment Variables

You can customize the configuration by modifying `docker-compose.yml` or creating a `.env` file:

```env
DB_URL=jdbc:postgresql://postgres:5432/hospitaldb
DB_USERNAME=hospitaluser
DB_PASSWORD=hospitalpass
JWT_SECRET=your-secret-key-here
```

## Troubleshooting

### Port Already in Use
If port 8081 is already in use:
```bash
# Stop the existing application
docker-compose down

# Or change the port in docker-compose.yml:
# ports:
#   - "8082:8081"  # Use 8082 externally, 8081 internally
```

### Database Connection Issues
```bash
# Check if database is healthy
docker-compose ps

# Check database logs
docker-compose logs postgres

# Restart services
docker-compose restart
```

### Application Won't Start
```bash
# Check application logs
docker-compose logs hospital-app

# Rebuild from scratch
docker-compose down -v
docker-compose up --build
```

### Clear Everything and Start Fresh
```bash
# Remove containers, volumes, and images
docker-compose down -v --rmi all

# Rebuild and start
docker-compose up --build
```

## API Endpoints Summary

See `API_ENDPOINTS.md` for complete documentation.

### Public Endpoints
- `POST /auth/register` - Register user
- `POST /auth/login` - Login
- `GET /swagger-ui/index.html` - API docs

### Protected Endpoints (Require JWT)
- `GET/POST/PUT/DELETE /appointments` - Appointment management
- `GET/POST /doctors` - Doctor management (Admin only)
- `GET /users` - Get all users
- `GET /analytics/*` - Analytics endpoints
- `GET /api/weather` - Weather information

## Notes

- Database data persists in Docker volume `postgres_data`
- File uploads persist in Docker volume `uploads_data`
- Application automatically creates database tables on first startup (Hibernate DDL auto-update)
- JWT tokens expire after 24 hours (86400000 ms)
