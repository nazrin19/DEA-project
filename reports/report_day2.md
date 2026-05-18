# 📅 DEA Project — Daily Report
## Day 02 | Monday, 18 May 2026
**Reported by:** Person 6

---

## ✅ Team Progress Summary

| Person | What Was Done | Branch | Status |
|--------|--------------|--------|--------|
| Nazrina | Merged all branches into main, fixed issues found during merge, reviewed team code (99% good), instructed team to always pull main before coding, warned not to run app until features complete | main | ✅ Active |
| Person 4 | Created AdminController, UserService (suspend + approve), AdminStatsDto. All pushed to GitHub | person-4 | ✅ Done |
| Person 5 | Configured SMTP, completed EmailService, BookingService, BookingController, RentalReminderScheduler. Fixed Booking entity and BookingRepository issues. All pushed | person-5-email | ✅ Done |
| Person 6 | Posted weekly goals to group. Started data.sql — users section completed | person-6 | 🔄 In Progress |

---

## 📝 Detailed Updates

### Person 4 — Admin Panel
- Created `AdminController.java` — manages all admin routes (dashboard, users, tool approvals, bookings)
- Created `UserService.java` — handles suspending users and approving shop owners
- Created `AdminStatsDto.java` — holds dashboard stats (total users, tools, bookings, pending approvals)
- All files pushed to `person-4` branch

### Person 5 — Email Notifications
- Configured Gmail SMTP in `application.properties`
- Completed `EmailService.java` for sending notification emails
- Added booking logic in `BookingService` and created `BookingController`
- Fixed Booking entity and BookingRepository issues including custom query method
- Completed `RentalReminderScheduler` — automated daily job for return reminders
- All files pushed to `person-5-email` branch

### Person 6 — QA & Data
- Posted weekly goals to group chat
- Started `data.sql` — users section completed (1 admin, 3 shop owners, 5 customers)
- Monitoring team progress and flagging blockers

---

## 📢 Important: Git Workflow Rules (by Nazrina)

Nazrina merged all branches into main and fixed issues. Team must follow these rules:

1. Code your feature on your local branch
2. Push your branch → `git push origin your-branch-name`
3. Open GitHub → click **New Pull Request** → merge into `main`
4. Always **pull from main before starting your coding** — otherwise it will cause issues
5. **Do NOT run the app today** — unfinished features will cause it to fail ⚠️
6. If you make changes to **security code**, inform Nazrina first

---

## ⚠️ Blockers
- User Auth is still pending — without login, no one can fully test their features
- Tool and Booking entities still need to be started

---

## 📋 Plan for Tomorrow — Day 03 (Tuesday 20 May)

| Person | Target |
|--------|--------|
| P1 | Register + Login + UserService + AuthController + role-based redirect |
| P2 | Tool entity, ToolRepository, ToolService, ToolController |
| P3 | Booking entity, BookingStatus enum, BookingRepository skeleton |
| P4 | Admin user list + suspend endpoint + all bookings endpoint |
| P5 | Email triggers — booking created, confirmed, rejected |
| P6 | Complete data.sql (tools + bookings), check all branches, write Day 03 report |

---

*DEA Project | Day 02 Report | Submitted by Person 6*
