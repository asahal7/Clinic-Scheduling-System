# 🏥 Clinic Scheduling & Waitlist Engine

A backend scheduling system for managing clinic appointments with strict overlap prevention, validation, and RESTful APIs.

Built using **Java, Spring Boot, PostgreSQL, Flyway, and JPA**, this project simulates a production-style backend service for handling medical appointment scheduling.

The system ensures that **clinicians cannot be double-booked**, supports **pagination and filtering**, and provides **structured API error handling**.

---

# 🚀 Features

## Appointment Scheduling
- Create appointments for patients with clinicians
- Prevent overlapping appointments for the same clinician
- Validate time ranges and input data
- Automatically generate unique appointment IDs (UUID)

## Advanced API Capabilities
- Pagination for large appointment datasets
- Filtering appointments by clinician or patient
- Structured JSON error responses
- DTO-based API responses (no entity leakage)

## Database & Persistence
- PostgreSQL relational database
- Flyway database migrations
- JPA / Hibernate ORM mapping
- Connection pooling with HikariCP

## Production-style backend practices
- Layered architecture
- Global exception handling
- Integration testing
- RESTful API design

---

# 🏗️ Architecture

The application follows a standard **layered backend architecture**:

Controller Layer │ ▼ Service Layer (business logic) │ ▼ Repository Layer (Spring Data JPA) │ ▼ PostgreSQL Database


### Key Components

| Layer | Responsibility |
|------|----------------|
| Controller | Handles HTTP requests and responses |
| Service | Business logic and validation |
| Repository | Database access using JPA |
| Domain | Core entities (Appointment, Patient, Clinician) |
| DTO | Clean API response models |
| Exception Handler | Centralised API error responses |

---
## 📂 Project Structure

```text
src/
└── main/
    └── java/com/abdimaalik/clinic/
        ├── controller/
        │   ├── AppointmentController.java
        │   └── GlobalExceptionHandler.java
        │
        ├── service/
        │   └── ClinicService.java
        │
        ├── repository/
        │   └── AppointmentRepository.java
        │
        ├── domain/
        │   └── Appointment.java
        │
        ├── dto/
        │   ├── AppointmentResponseDTO.java
        │   └── ErrorResponse.java
        │
        └── ClinicSchedulingApplication.java

src/
└── main/
    └── resources/
        └── db/migration/
            ├── V1__create_appointments_table.sql
            ├── V2__add_fee_to_appointments.sql
            └── V3__...

---
```

## ⚙️ Tech Stack

| Technology | Purpose |
|-----------|---------|
| Java 21 | Primary language for backend development |
| Spring Boot | Application framework for building RESTful services |
| Spring Web | HTTP request handling and API layer |
| Spring Data JPA | ORM for database interaction and repository abstraction |
| PostgreSQL | Relational database for persistent storage |
| Flyway | Version-controlled database schema migrations |
| Redis | In-memory caching layer to improve read performance |
| Maven | Build automation and dependency management |
| JUnit & Spring Test | Integration and application-layer testing |

---

# 📡 API Endpoints

## Create Appointment

POST /appointments


Example request:

```json
{
  "patientName": "Alice Johnson",
  "clinicianName": "Dr Smith",
  "startTime": "2026-03-20T10:00:00",
  "endTime": "2026-03-20T10:30:00",
  "fee": 75.00
}

Example response:

{
  "id": "9b5d1c21-3b44-4d1a-9a20-cc2d97fa87a1",
  "patientName": "Alice Johnson",
  "clinicianName": "Dr Smith",
  "startTime": "2026-03-20T10:00:00",
  "endTime": "2026-03-20T10:30:00",
  "fee": 75.00
}


Get All Appointments
Supports pagination.

GET /appointments?page=0&size=10


Filter Appointments
Filter by clinician:

GET /appointments?clinicianName=Dr Smith

Filter by patient:

GET /appointments?patientName=Alice Johnson


Get Appointment by ID

GET /appointments/{id}


Delete Appointment

DELETE /appointments/{id}


❌ Error Handling
The API returns structured error responses when requests fail.
Example:

{
  "timestamp": "2026-03-19T10:12:34",
  "message": "Appointment overlaps with an existing appointment"
}

Errors are handled centrally via the GlobalExceptionHandler.

🔄 Database Migrations
The project uses Flyway for versioned schema migrations.
Example migrations:

V1__create_appointments_table.sql
V2__add_fee_to_appointments.sql

Flyway automatically applies migrations at application startup.

🧪 Testing
Integration tests verify:
Appointment creation
Overlap prevention
API endpoints
Database persistence
Example test class:

AppointmentControllerIntegrationTest

Run tests with:

mvn test


▶️ Running the Project Locally
1. Clone repository

git clone https://github.com/asahal7/Clinic-Scheduling-System.git

2. Start PostgreSQL
Create the database:

CREATE DATABASE clinic_db;

3. Configure application.properties
Example configuration:

spring.datasource.url=jdbc:postgresql://localhost:5432/clinic_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true

4. Run the application

mvn spring-boot:run

Server starts at:

http://localhost:8080


🧠 Backend Concepts Demonstrated
This project demonstrates practical backend engineering concepts including:
REST API design
layered architecture
database migrations
input validation
conflict detection (scheduling overlaps)
DTO usage
centralized exception handling
integration testing
pagination and filtering


👨‍💻 Author
Abdimaalik Sahal
Computer Science & Mathematics Queen Mary University of London

