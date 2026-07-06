# LankaTools 🔧

**LankaTools** is a B2B tool and heavy equipment rental platform built for Sri Lanka. It connects shop owners who list construction, cleaning, and industrial tools with customers who need to rent them, with an admin layer to moderate the marketplace.

## Features

- **Role-based access** for three user types:
  - **Admin** – approves/rejects shops and tool listings, moderates users, views platform-wide stats
  - **Shop Owner** – lists tools, manages rental requests, tracks their own inventory
  - **Customer** – browses the tool catalog, books equipment, and manages their bookings
- **Tool catalog** with categories, daily rates, images, and approval workflow (`PENDING` → `APPROVED` / `REJECTED`)
- **Booking system** with status tracking (`PENDING`, `CONFIRMED`, `ACTIVE`, `RETURNED`, `REJECTED`, `CANCELLED`)
- **Email notifications** for booking updates and rental reminders
- **Secure authentication** with Spring Security and role-based authorization
- **File uploads** for tool images

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Persistence | Spring Data JPA / Hibernate |
| Database | MySQL |
| Security | Spring Security |
| Templating | Thymeleaf |
| Build Tool | Maven |
| Email | Spring Boot Mail (SMTP) |

## Project Structure

```
src/main/java/com/example/Lankatools/
├── config/          # Security & auth configuration
├── controller/      # REST & view controllers (Admin, Auth, Booking, Tool, User, etc.)
├── dto/             # Data transfer objects
├── entity/          # JPA entities (User, Tool, Booking)
├── enums/           # Role, BookingStatus, ToolStatus
├── exception/       # Global exception handling
├── repository/      # Spring Data repositories
└── service/         # Business logic (booking, email, user, reminders)

src/main/resources/
├── data.sql         # Seed data
├── static/          # Images, JS, uploads
└── templates/       # Thymeleaf views (admin/, customer/, owner/, fragments/)
```

## Getting Started

### Prerequisites
- Java 21+
- Maven (or use the included `mvnw` wrapper)
- MySQL Server running locally

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/nazrin19/DEA-project.git
   cd DEA-project
   ```

2. **Configure the database**

   Create a MySQL database (or let it auto-create) and update `src/main/resources/application.properties` with your local credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/lankatools?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

3. **Configure email (optional, for notifications)**

   Set your own SMTP credentials in `application.properties` (do not commit real credentials):
   ```properties
   spring.mail.username=your_email@gmail.com
   spring.mail.password=your_app_password
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```
   On Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

5. **Open in browser**
   ```
   http://localhost:8080
   ```

### Seed Data

`data.sql` seeds sample users, tools, and bookings. All seeded user passwords resolve to `password123`. Seeding runs based on `spring.sql.init.mode` in `application.properties` — set it to `always` if you want the seed data loaded on startup (and note it may attempt duplicate inserts on repeated runs).

## Branching & Contribution Workflow

This project uses a feature-branch workflow:
1. Create a branch off `main` for your feature (e.g., `person-x-feature`)
2. Commit and push your changes to your branch
3. Open a Pull Request into `main`
4. A reviewer merges once approved

Please sync your branch with `main` regularly to avoid large merge conflicts.

## License

This project is for educational purposes as part of a group coursework assignment.
