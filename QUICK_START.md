# Quick Start Guide - Docker & Postman Testing

## ✅ Application Status
The Hospital Management System is now running in Docker!

**Base URL:** `http://localhost:8081`  
**Swagger UI:** `http://localhost:8081/swagger-ui/index.html`

---

## 🐳 Docker Commands

### Start the Application
```bash
docker-compose up -d
```

### Stop the Application
```bash
docker-compose down
```

### View Logs
```bash
# All services
docker-compose logs -f

# Application only
docker-compose logs -f hospital-app

# Database only
docker-compose logs -f postgres
```

### Rebuild After Code Changes
```bash
docker-compose up --build -d
```

### Check Container Status
```bash
docker-compose ps
```

---

## 📮 Postman Testing Guide

### Step 1: Register a User
**Request:**
- **Method:** `POST`
- **URL:** `http://localhost:8081/auth/register`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "role": "PATIENT"
}
```

**Expected Response:** `200 OK` - "User registered successfully"

---

### Step 2: Login and Get Token
**Request:**
- **Method:** `POST`
- **URL:** `http://localhost:8081/auth/login`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "username": "testuser",
  "password": "password123"
}
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "testuser",
  "role": "PATIENT"
}
```

**⚠️ IMPORTANT:** Copy the `token` value for next steps!

---

### Step 3: Use Token in Protected Endpoints

Add this header to all subsequent requests:
```
Authorization: Bearer {your-token-here}
```

---

### Step 4: Test Protected Endpoints

#### Get All Appointments
- **Method:** `GET`
- **URL:** `http://localhost:8081/appointments`
- **Headers:** `Authorization: Bearer {token}`

#### Create Appointment
- **Method:** `POST`
- **URL:** `http://localhost:8081/appointments`
- **Headers:** 
  - `Authorization: Bearer {token}`
  - `Content-Type: application/json`
- **Body:**
```json
{
  "doctorId": 1,
  "patientId": 1,
  "appointmentDate": "2025-02-15T10:00:00",
  "reason": "Regular checkup",
  "status": "SCHEDULED"
}
```

#### Get Analytics
- **Method:** `GET`
- **URL:** `http://localhost:8081/analytics/status-count`
- **Headers:** `Authorization: Bearer {token}`

#### Get All Users
- **Method:** `GET`
- **URL:** `http://localhost:8081/users`
- **Headers:** `Authorization: Bearer {token}`

---

## 🔐 Admin Endpoints

To test admin endpoints, first register a user with `"role": "ADMIN"`:

### Create Doctor (Admin Only)
- **Method:** `POST`
- **URL:** `http://localhost:8081/doctors`
- **Headers:** 
  - `Authorization: Bearer {admin-token}`
  - `Content-Type: application/json`
- **Body:**
```json
{
  "name": "Dr. John Smith",
  "specialization": "Cardiology",
  "email": "dr.smith@hospital.com",
  "phone": "+1234567890"
}
```

---

## 📋 Complete API List

See `API_ENDPOINTS.md` for the complete list of all available endpoints.

---

## 🐛 Troubleshooting

### Application Not Responding
```bash
# Check if containers are running
docker-compose ps

# Check application logs
docker-compose logs hospital-app

# Restart services
docker-compose restart
```

### Database Connection Issues
```bash
# Check database logs
docker-compose logs postgres

# Restart database
docker-compose restart postgres
```

### Port Already in Use
If port 8081 is already in use, edit `docker-compose.yml`:
```yaml
ports:
  - "8082:8081"  # Change external port to 8082
```

### Clear Everything and Start Fresh
```bash
docker-compose down -v
docker-compose up --build -d
```

---

## 📝 Notes

- **Database:** PostgreSQL running in Docker (data persists in volume)
- **JWT Tokens:** Valid for 24 hours
- **Rate Limiting:** Appointment creation limited to 10 requests/minute
- **File Uploads:** Stored in Docker volume `uploads_data`
- **Swagger UI:** Interactive API documentation at `/swagger-ui/index.html`

---

## 🎯 Quick Test Checklist

- [ ] Application is running (`docker-compose ps`)
- [ ] Swagger UI accessible (`http://localhost:8081/swagger-ui/index.html`)
- [ ] Register a user successfully
- [ ] Login and get JWT token
- [ ] Use token to access protected endpoints
- [ ] Create an appointment
- [ ] View analytics
- [ ] (Optional) Test admin endpoints with ADMIN role

---

Happy Testing! 🚀
