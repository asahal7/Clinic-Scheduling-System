# Clinic Scheduling & Waitlist Engine (Java + Spring Boot)

A backend system for managing clinic appointments with conflict detection, waitlists, and automated reminders.

## Core Features
- Book appointments (doctor + patient)
- Cancel / reschedule appointments
- Automatic overlap + conflict detection
- Waitlist system when slots are full
- Background reminders (job queue)

## Tech Stack
- Java 21
- Spring Boot (REST API)
- Maven (build + dependency management)
- PostgreSQL (SQL database)
- Flyway (schema migrations)
- GitHub Actions CI

## Run Locally (later)

```bash
docker compose up -d
mvn spring-boot:run

