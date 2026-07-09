# LankaTools 🛠️

**LankaTools** is a specialized B2B heavy equipment and construction tool rental platform designed for Sri Lanka. The application connects local shop owners who list heavy machinery (such as concrete mixers, generators, and scaffolding) with contractors and customers who need to rent them, featuring a moderated administrative workflow layer.

---

## 🚀 Key Features

### 🔐 1. Role-Based Access Control (RBAC)
The platform dynamically shifts views and access controls based on three distinct authenticated user profiles:
* **Administrator:** Moderates the entire marketplace, tracks platform stats, and updates tool registration approvals (`PENDING` ➔ `APPROVED` / `REJECTED`).
* **Shop Owner:** Lists industrial inventory, manages rental pricing grids, and keeps track of incoming booking schedules.
* **Customer:** Browses the equipment catalog, filters listings, and books tools for rental durations.

### 💼 2. Core Functional Pillars
* **Dynamic Inventory System:** Tracks active tool states and rental constraints.
* **State-Driven Booking Lifecycle:** Handles transactions smoothly across states (`PENDING`, `CONFIRMED`, `ACTIVE`, `RETURNED`, `REJECTED`, `CANCELLED`).
* **Automated Admin Seeding:** Utilizes a secure, runtime `CommandLineRunner` hook to auto-provision default administrative credentials safely upon cluster initialization.

---

## 🛠️ Technical Architecture

* **Language & Core Platform:** Java 21 & Spring Boot
* **Security & Sessions:** Spring Security (with BCrypt password hashing and stateful HTTP session serialization)
* **Template Engine:** Thymeleaf
* **UI Framework:** Bootstrap 5 (Responsive Layout Engine)
* **Persistence & Mapping:** Spring Data JPA & Hibernate ORM
* **Cloud Production Database:** Aiven MySQL Cluster
* **Hosting & Continuous Deployment:** Railway Cloud Platform

---

## ⚙️ Environment Setup & Installation

### Local Prerequisites
1. Ensure **XAMPP** is installed and running the **MySQL** module locally on port `3306`.
2. Clone this repository to your local directory.

### Configuration
Update your local `src/main/resources/application.properties` connection pool:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lankatools?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update