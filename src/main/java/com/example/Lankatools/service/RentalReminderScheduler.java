package com.example.Lankatools.service;

import com.example.Lankatools.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class RentalReminderScheduler {

    @Autowired
    private BookingRepository bookingRepository; // Managed by your group member (Person 03)

    @Autowired
    private EmailService emailService; // Your custom service built earlier

    /**
     * This cron expression triggers the function automatically every single day at 8:00 AM
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendReturnReminders() {
        // 1. Calculate what tomorrow's date is
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        System.out.println("Automated Scheduler Woke Up! Checking for bookings ending on: " + tomorrow);

        // Note: Your team member managing BookingRepository needs to create the findByEndDateAndStatus method!
        // For now, we will comment this structure out so your app can compile without errors if they haven't written it yet.
        /*
        List<Booking> dueBookings = bookingRepository.findByEndDateAndStatus(tomorrow, "ACTIVE");

        for (Booking booking : dueBookings) {
            String customerEmail = booking.getCustomer().getEmail();
            String message = "Hi " + booking.getCustomer().getName() + ",\n\n" +
                             "This is a reminder that your rental tool [" + booking.getTool().getName() +
                             "] is due for return tomorrow. Please return it on time!";

            emailService.sendSimpleEmail(customerEmail, "Tool Return Reminder", message);
        }
        */
    }
}