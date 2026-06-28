# Backend Phase Summary Report
**Project:** LankaTools
**Period:** 17th May 2026 – 1st June 2026 (Day 1 – Day 9)
**Prepared by:** Person 6

---

## Overview

This report summarizes the backend development phase of the LankaTools project, covering the period from project kickoff (17 May) through the official unblocking of frontend work (1 June). Backend modules across Auth, Tools, Bookings, Admin, and Email are complete, and the team has been cleared to begin frontend implementation as of Day 9.

---

## Timeline of Backend Progress

### Day 1 — 17/05/2026
- Nazrina set up the GitHub repo and instructed the team to clone and begin work, reminding everyone not to push directly to `main`
- Person-1 implemented user registration with validation and password hashing, modifying Nazrina's base code; verified working via Postman
- Person-2 cloned the project, completed the Tool entity, ToolStatus enum, and ToolRepository, and fixed a local XAMPP issue
- Person-3 cloned the project and completed the Booking entity and BookingStatus enum
- Blocker: Person-3 couldn't locate the application password needed to test on localhost

### Day 2 — 18/05/2026
- Nazrina merged all branches into `main`, fixed issues found during the merge, and reviewed the team's code (rated ~99% good)
- Set firm Git workflow rules: always pull `main` before coding, push to your own branch then open a PR, do not run the app until features are complete, and inform Nazrina before changing security code
- Person-4 completed `AdminController`, `UserService` (suspend/approve), and `AdminStatsDto`
- Person-5 configured Gmail SMTP, completed `EmailService`, fixed Booking entity/repository issues, added booking logic to `BookingService`/`BookingController`, and finished the automated `RentalReminderScheduler`
- Person-6 posted the team's weekly goals and began `data.sql` (users section: 1 admin, 3 shop owners, 5 customers)

### Day 3 — 19/05/2026
- Person-2's Tool module reached 100% backend completion — full CRUD endpoints, pagination/sorting, secure owner-binding on save, image upload with file-type and 2MB size validation, and admin approve/reject routing
- Person-3 completed the Booking module backend — entity, enum, repository, service, controller, date-overlap validation, and total cost calculation, with validations checked
- Person-5 finished `EmailService` logic and the automated scheduler, using a mock `JavaMailSender` due to SMTP restrictions, with email details logged locally for testing
- Nazrina reviewed a pull request from Vinuji, requested changes, and reminded the team to always pull `main` first and never delete existing code

### Day 4 — 22/05/2026
- Person-1 completed login and registration logic with BCrypt password hashing and session management
- Person-4 finished all Admin features and resolved a merge conflict; local phpMyAdmin database setup still in progress
- Nazrina confirmed `main` was runnable with a live, visible homepage, and asked the team to pull and test, noting that testing midway makes issues easier to fix
- Person-6 completed the full `data.sql` seed file and uploaded it to GitHub: 1 admin, 3 shop owners, 5 customers, 15 tools (APPROVED/PENDING/REJECTED), 10 bookings (various statuses)
- No blockers reported

### Day 5 — 23/05/2026
- Nazrina merged Vino's branch into `main` with no conflicts, and reviewed and verified the SQL seed data file as correct and ready
- `main` confirmed stable and pullable for the whole team
- No blockers reported; updates still pending from Person-1 through Person-5

### Day 6 — 24/05/2026
- Person-6 implemented the CSV export feature (`ExportController.java`) — an admin-only endpoint (`GET /api/admin/export/bookings`) exporting all bookings as a downloadable CSV, with null-safe checks, UTF-8 encoding, and proper CSV escaping
- Person-3 successfully ran the full Spring Boot application on port 8080 and tested all 6 booking API endpoints via Postman (creation, customer retrieval, shop owner retrieval, confirm, reject, cancellation) — all returned 200 OK
- Nazrina merged Sai's branch and requested the shared `layout.html` navbar be added to the homepage for testing

### Day 7 — 26/05/2026
- Nazrina began inserting the seed data and asked the team to hold off on frontend work until this was complete
- Confirmed she would upload a frontend file structure before anyone started frontend coding, to keep the team consistent
- Flagged that custom CSS would still be needed alongside Bootstrap (e.g. for the image bar), that some backend modules use the JSON fetch method (relevant to whoever builds the matching frontend), and asked the team to settle on a theme and create a logo

### Day 8 — 29/05/2026
- Login fully fixed after a difficult debugging session; data confirmed flowing correctly
- Nazrina flagged the need for more careful testing going forward now that login/data flow is live, and said she would document the errors she encountered so the team could learn from them
- Needed more time before pushing the code, wanting to give the team clear instructions first; asked the team to use the waiting time to plan their frontend code rather than stay idle

### Day 9 — 1/06/2026
- **Login confirmed fully working and ready to pull**
- Asked the team to clear any local SQL/phpMyAdmin cache before running the app
- **Frontend work officially unblocked from this day** — team given a 10-day target to complete it
- No single fixed frontend file structure was implemented yet; some existing packages (e.g. `admin`) could be reused, but the team was asked to organize new classes properly rather than placing everything flat inside `templates/`
- Flagged that significant recent changes were made across the codebase, and asked the team to check their own code for anything accidentally removed

---

## Backend Status by Module

| Module | Owner | Status |
|---|---|---|
| Auth (register/login/session) | Person-1 | ✅ Complete |
| Tools (CRUD, image upload, approval) | Person-2 | ✅ Complete |
| Bookings (CRUD, overlap validation, cost calc) | Person-3 | ✅ Complete, API-tested (all 200 OK) |
| Admin (user/tool/booking management) | Person-4 | ✅ Complete |
| Email notifications + scheduler | Person-5 | ✅ Complete |
| Seed data (`data.sql`) | Person-6 | ✅ Complete |
| CSV export (bookings) | Person-6 | ✅ Complete |
| Security config / Git workflow / merges | Nazrina | ✅ Ongoing, stable |

---

## Outstanding Items Going Into the Frontend Phase
- No unified frontend file structure was handed down until Day 10 — backend phase ended with the team still waiting on this
- Theme and logo were flagged as needed on Day 7 but not confirmed finalized within the backend phase
- Some recent backend changes by Nazrina (Day 9) hadn't been individually verified against each person's existing work yet
- Postman collection documentation was tested module-by-module (e.g. Person-3's bookings) but not yet consolidated into one shared collection

---

## Conclusion

Backend development for LankaTools is complete as of 1 June 2026 (Day 9). All five core modules — Auth, Tools, Bookings, Admin, and Email — are implemented, individually tested at the API level, and merged into `main`. Seed data and CSV export, both Person-6 deliverables, are also complete. The team was cleared to begin frontend work from Day 9 onward, with a 10-day target window, and Day 10 marks the actual start of that frontend phase.

---

*LankaTools Project | Person 6 | Backend Phase Summary (Day 1–9)*
