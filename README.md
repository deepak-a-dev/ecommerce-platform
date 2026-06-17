# 🛒 E-Commerce Platform

A full-stack e-commerce application. The backend — a Spring Boot REST API — started out
as my final-year college project; I've since taken it up to modernize it and grow it into
a complete full-stack application. A web frontend, containerization, and a live deployment
are on the way.

![Status](https://img.shields.io/badge/status-in%20development-orange)
![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-6DB33F?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-database-4479A1?logo=mysql&logoColor=white)

> 🚧 **Status: actively under development.** This project is being built and modernized
> in incremental phases — see the roadmap below.

---

## 🧰 Tech Stack

**Backend (current)**
- Java 17 · Spring Boot 3.2
- Spring Security with JWT authentication
- Spring Data JPA / Hibernate
- MySQL
- Maven

**Planned**
- Frontend SPA (React or Next.js — to be decided)
- Docker & Docker Compose
- CI/CD with GitHub Actions
- Cloud deployment

---

## ✅ Current Features (Backend API)

- User registration & login with JWT-based authentication
- Role-based access control (USER / ADMIN)
- Product catalog — create, update, remove, and browse by category
- Order placement with line items and order totals
- Forgot-password flow via emailed one-time password (OTP)

---

## 🗺️ Roadmap

**Phase 0 — Project setup & version control** ✅ Done

### Phase 1 — Backend modernization & hardening
- [ ] Return `ResponseEntity<DTO>` with correct HTTP status codes
- [ ] Global exception handling via `@RestControllerAdvice`
- [ ] Bean Validation (`@Valid`) on all request DTOs
- [ ] Response DTOs everywhere (never expose entities directly)
- [ ] Bug fixes, transactional order placement, and stock/inventory tracking
- [ ] Admin bootstrapping (seeded admin or promote endpoint)
- [ ] Flyway database migrations
- [ ] OpenAPI / Swagger documentation
- [ ] Unit & integration tests (Testcontainers)

### Phase 2 — New features
- [ ] Persistent cart
- [ ] Order history
- [ ] Product search + pagination + category filter
- [ ] Refresh tokens
- [ ] (Optional) Stripe test-mode checkout, product image upload

### Phase 3 — Frontend
- [ ] Auth (login / register, JWT attached to requests)
- [ ] Product catalog grid + product detail
- [ ] Cart + checkout
- [ ] Order history
- [ ] Role-gated admin dashboard
- [ ] Responsive styling

### Phase 4 — DevOps & deployment
- [ ] Dockerize backend + frontend
- [ ] `docker-compose` (backend + frontend + MySQL)
- [ ] GitHub Actions CI (build + test)
- [ ] Deploy a live demo

### Phase 5 — Presentation polish
- [ ] Comprehensive README (live demo link, screenshots, architecture diagram, setup)
- [ ] (Optional) demo video, license

### Suggested execution order
Phase 0 → 1 → (start Phase 3 in parallel once API contracts are stable) → 2 → 4 → 5

### Open decisions
- **Frontend framework:** React + Vite + TypeScript vs. Next.js
- **Deployment:** full hosted live demo vs. Docker-only local

---

## 🚀 Getting Started (local)

> Requires JDK 17+ and a running MySQL instance.

1. Create the database schema named `ecommerce_platform`.
2. Set the required environment variables — see [`backend/.env.example`](backend/.env.example).
3. From the `backend/` directory, start the app:
   ```bash
   ./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
   ```
4. The API will be available at `http://localhost:8080`.
