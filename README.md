# Clinic Scheduling & Waitlist Engine

A backend appointment scheduling system built in Java and Spring Boot, designed around the concurrency and correctness problems that make booking systems hard in practice — double-booking prevention, waitlist automation, and consistent state under concurrent requests.

---

## Architecture

```
┌─────────────────┐        ┌──────────────────┐        ┌──────────────┐
│   REST Client   │───────▶│  Spring Boot API  │───────▶│  PostgreSQL  │
└─────────────────┘        └──────────────────┘        └──────────────┘
                                    │
                                    ▼
                             ┌──────────────┐
                             │    Redis     │
                             │   (Cache)    │
                             └──────────────┘
```

**Controller Layer** — handles HTTP requests, input validation, and structured error responses.

**Service Layer** — business logic, overlap prevention, waitlist promotion, and invariant enforcement.

**Repository Layer** — Spring Data JPA with pessimistic locking for concurrent booking safety.

---

## Key Engineering Decisions

**Pessimistic locking** — double-booking is prevented at the database level via SELECT FOR UPDATE. This ensures two simultaneous booking requests for the same clinician slot cannot both succeed, regardless of timing.

**Waitlist engine** — when an appointment is cancelled, the system automatically promotes the next waitlisted patient into the freed slot. Promotion is transactional to prevent race conditions.

**Redis cache-aside pattern** — read-heavy endpoints are served from Redis where possible. Cache is invalidated on booking, cancellation, or rescheduling. TTL is set to 10 minutes.

**Flyway migrations** — schema changes are versioned and applied automatically at startup, ensuring reproducible environments across local and CI.

**DTO boundary** — entities are never leaked through the API layer. All responses use DTOs with a clean separation between persistence and transport models.

**Appointment lifecycle** — appointments move through a defined set of states: `CONFIRMED` → `COMPLETED`, `CANCELLED`, or `NO_SHOW`. Status transitions are enforced in the service layer.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot |
| Database | PostgreSQL |
| ORM | Spring Data JPA + Hibernate |
| Cache | Redis |
| Migrations | Flyway |
| Build | Maven |
| Testing | JUnit 5 · Spring Integration Tests |
| Containerisation | Docker + Docker Compose |

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/appointments` | Book a new appointment |
| GET | `/appointments` | List appointments (paginated, filterable) |
| GET | `/appointments/{id}` | Get appointment by ID |
| PATCH | `/appointments/{id}/cancel` | Cancel an appointment |
| PATCH | `/appointments/{id}/complete` | Mark an appointment as completed |
| PATCH | `/appointments/{id}/no-show` | Mark an appointment as no-show |

Filtering and sorting:

```
GET /appointments?clinicianName=Dr Smith
GET /appointments?patientName=Alice Johnson
GET /appointments?status=CONFIRMED
GET /appointments?page=0&size=10&sortBy=startTime&direction=asc
```

Example request:

```json
{
  "patientName": "Alice Johnson",
  "clinicianName": "Dr Smith",
  "startTime": "2026-03-20T10:00:00",
  "endTime": "2026-03-20T10:30:00",
  "fee": 75.00
}
```

Example response:

```json
{
  "id": "9b5d1c21-3b44-4d1a-9a20-cc2d97fa87a1",
  "patientName": "Alice Johnson",
  "clinicianName": "Dr Smith",
  "startTime": "2026-03-20T10:00:00",
  "endTime": "2026-03-20T10:30:00",
  "fee": 75.00,
  "status": "CONFIRMED"
}
```

Error responses are structured and centralised via `GlobalExceptionHandler`:

```json
{
  "timestamp": "2026-03-19T10:12:34",
  "message": "Appointment overlaps with an existing appointment"
}
```

---

## Project Structure

```
src/main/java/com/abdimaalik/clinic/
├── config/
├── controller/
├── domain/
├── dto/
├── exception/
├── repository/
├── service/
└── ClinicSchedulingApplication.java

src/main/resources/db/migration/
├── V1__create_appointments_table.sql
├── V2__add_fee_to_appointments.sql
└── V3__add_appointment_status.sql
```

---

## Running Locally

### Option 1: Docker Compose (recommended)

Prerequisites: Docker and Docker Compose installed.

```
git clone https://github.com/asahal7/clinic-scheduling-system.git
cd clinic-scheduling-system
docker compose up --build
```

Server starts at http://localhost:8081. PostgreSQL and the application container are configured automatically.

### Option 2: Maven + local PostgreSQL

Prerequisites: PostgreSQL running locally.

```
git clone https://github.com/asahal7/clinic-scheduling-system.git
cd clinic-scheduling-system

psql -U postgres -c "CREATE DATABASE clinic_db;"
```

Configure `src/main/resources/application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/clinic_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
```

Run the application:

```
mvn spring-boot:run
```

Server starts at http://localhost:8080. Flyway applies all migrations automatically on startup.

Run tests:

```
mvn test
```

---

## Connect

- LinkedIn: [abdimaalik-sahal](https://linkedin.com/in/abdimaalik-sahal-33bbab336/)
- GitHub: [asahal7](https://github.com/asahal7)
