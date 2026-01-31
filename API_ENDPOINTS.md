# Hospital Management System - API Endpoints

## Base URL
```
http://localhost:8081
```

## Swagger UI
```
http://localhost:8081/swagger-ui.html
```

---

## Authentication Endpoints (Public)

### 1. Register User
- **Method:** `POST`
- **URL:** `/auth/register`
- **Body:**
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "role": "PATIENT" | "ADMIN"
}
```
- **Response:** `200 OK` - "User registered successfully"

### 2. Login
- **Method:** `POST`
- **URL:** `/auth/login`
- **Body:**
```json
{
  "username": "string",
  "password": "string"
}
```
- **Response:** 
```json
{
  "token": "jwt-token-here",
  "username": "string",
  "role": "PATIENT" | "ADMIN"
}
```

---

## Appointment Endpoints (Requires Authentication)

### 3. Create Appointment
- **Method:** `POST`
- **URL:** `/appointments`
- **Headers:** `Authorization: Bearer {token}`
- **Body:**
```json
{
  "doctorId": 1,
  "patientId": 1,
  "appointmentDate": "2025-02-01T10:00:00",
  "reason": "string",
  "status": "SCHEDULED" | "CONFIRMED" | "COMPLETED" | "CANCELLED"
}
```
- **Note:** Rate limited (10 requests per minute)

### 4. Get Appointment by ID
- **Method:** `GET`
- **URL:** `/appointments/{id}`
- **Headers:** `Authorization: Bearer {token}`

### 5. Get All Appointments (Paginated)
- **Method:** `GET`
- **URL:** `/appointments?page=0&size=10&sort=appointmentDate,desc`
- **Headers:** `Authorization: Bearer {token}`
- **Query Parameters:**
  - `page`: Page number (default: 0)
  - `size`: Page size (default: 10)
  - `sort`: Sort field and direction (e.g., `appointmentDate,desc`)

### 6. Filter Appointments
- **Method:** `GET`
- **URL:** `/appointments/filter?doctorId=1&patientId=1&status=SCHEDULED&page=0&size=10`
- **Headers:** `Authorization: Bearer {token}`
- **Query Parameters:**
  - `doctorId`: (optional) Filter by doctor ID
  - `patientId`: (optional) Filter by patient ID
  - `status`: (optional) Filter by status (SCHEDULED, CONFIRMED, COMPLETED, CANCELLED)
  - `page`: Page number
  - `size`: Page size
  - `sort`: Sort field and direction

### 7. Update Appointment
- **Method:** `PUT`
- **URL:** `/appointments/{id}`
- **Headers:** `Authorization: Bearer {token}`
- **Body:** Same as Create Appointment

### 8. Delete Appointment
- **Method:** `DELETE`
- **URL:** `/appointments/{id}`
- **Headers:** `Authorization: Bearer {token}`
- **Response:** `200 OK` - "Appointment deleted successfully"

### 9. Upload Document
- **Method:** `POST`
- **URL:** `/appointments/{appointmentId}/upload`
- **Headers:** `Authorization: Bearer {token}`
- **Content-Type:** `multipart/form-data`
- **Body:** `file` (form-data)
- **Response:** `200 OK` - "File uploaded successfully"

---

## Doctor Endpoints (Requires ADMIN Role)

### 10. Create Doctor
- **Method:** `POST`
- **URL:** `/doctors`
- **Headers:** `Authorization: Bearer {token}` (Admin only)
- **Body:**
```json
{
  "name": "string",
  "specialization": "string",
  "email": "string",
  "phone": "string"
}
```

### 11. Get All Doctors
- **Method:** `GET`
- **URL:** `/doctors`
- **Headers:** `Authorization: Bearer {token}` (Admin only)

---

## User Endpoints (Requires Authentication)

### 12. Get All Users
- **Method:** `GET`
- **URL:** `/users`
- **Headers:** `Authorization: Bearer {token}`

---

## Analytics Endpoints (Requires Authentication)

### 13. Status-wise Appointment Count
- **Method:** `GET`
- **URL:** `/analytics/status-count`
- **Headers:** `Authorization: Bearer {token}`
- **Response:** Array of `[status, count]` pairs

### 14. Doctor-wise Appointment Count
- **Method:** `GET`
- **URL:** `/analytics/doctor-count`
- **Headers:** `Authorization: Bearer {token}`
- **Response:** Array of `[doctorId, count]` pairs

### 15. Daily Appointment Count
- **Method:** `GET`
- **URL:** `/analytics/daily`
- **Headers:** `Authorization: Bearer {token}`
- **Response:** Array of daily appointment statistics

---

## Weather API Endpoints (Requires Authentication)

### 16. Get Hospital Weather
- **Method:** `GET`
- **URL:** `/api/weather`
- **Headers:** `Authorization: Bearer {token}`
- **Response:** Weather data for hospital's default location

### 17. Get Weather by Location
- **Method:** `GET`
- **URL:** `/api/weather/location?latitude=40.7128&longitude=-74.0060`
- **Headers:** `Authorization: Bearer {token}`
- **Query Parameters:**
  - `latitude`: Latitude coordinate
  - `longitude`: Longitude coordinate

---

## Testing in Postman

### Setup:
1. Start the application (already running on port 8081)
2. Import the following collection or create requests manually

### Quick Test Flow:
1. **Register a user:**
   - POST `/auth/register`
   - Body: `{"username": "testuser", "email": "test@example.com", "password": "password123", "role": "PATIENT"}`

2. **Login:**
   - POST `/auth/login`
   - Body: `{"username": "testuser", "password": "password123"}`
   - Copy the `token` from response

3. **Use token in subsequent requests:**
   - Add header: `Authorization: Bearer {your-token-here}`

4. **Test endpoints:**
   - Create a doctor (requires ADMIN role)
   - Create an appointment
   - Get appointments
   - View analytics

### Note:
- All endpoints except `/auth/**` and Swagger UI require authentication
- Doctor endpoints require ADMIN role
- Rate limiting is applied to appointment creation (10 requests/minute)
