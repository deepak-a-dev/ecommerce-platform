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

**Backend**
- Java 17 · Spring Boot 3.2
- Spring Security with JWT (access + refresh tokens)
- Spring Data JPA / Hibernate
- MySQL · Flyway migrations
- Bean Validation · OpenAPI/Swagger (springdoc)
- JUnit 5 + Mockito (unit tests)
- Maven

**Planned**
- Frontend SPA (React or Next.js — to be decided)
- Docker & Docker Compose
- CI/CD with GitHub Actions
- Cloud deployment

---

## ✅ Current Features (Backend API)

- User registration & login with **JWT authentication + refresh tokens**
- Role-based access control (USER / ADMIN) with an auto-seeded admin
- Product catalog — create, update, remove, and **search / filter / paginate**
- **Shopping cart** — add, update, remove, clear
- **Checkout** → order placement with line items, totals, and stock decrement
- **Order history** scoped to the authenticated user
- Stock / inventory tracking
- Forgot-password flow via emailed one-time password (OTP)
- Consistent error responses, request validation, and interactive **Swagger** API docs

---

## 🗺️ Roadmap

**Phase 0 — Project setup & version control** ✅ Done

### Phase 1 — Backend modernization & hardening ✅ Done
- [x] Return `ResponseEntity<DTO>` with correct HTTP status codes
- [x] Global exception handling via `@RestControllerAdvice`
- [x] Bean Validation (`@Valid`) on all request DTOs
- [x] Response DTOs everywhere (never expose entities directly)
- [x] Bug fixes, transactional order placement, and stock/inventory tracking
- [x] Admin bootstrapping (seeded admin)
- [x] Flyway database migrations
- [x] OpenAPI / Swagger documentation
- [x] Unit tests (integration tests with Testcontainers planned for Phase 4)

### Phase 2 — New features ✅ Done
- [x] Persistent cart (add / update / remove / clear / checkout)
- [x] Order history
- [x] Product search + pagination + category filter
- [x] Refresh tokens
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
