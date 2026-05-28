package com.example.Lankatools.service;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class RentalReminderScheduler {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;

    /**
     * This cron expression triggers the function automatically every single day at 8:00 AM
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendReturnReminders() {
        // 1. Calculate what tomorrow's date is
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        System.out.println("Automated Scheduler Woke Up! Checking for bookings ending on: " + tomorrow);

        // 2. Fetch all bookings using built-in JPA methods
        List<Booking> allBookings = bookingRepository.findAll();

        // 3. Filter and process bookings due tomorrow
        for (Booking booking : allBookings) {
            // Updated to use getCustomer() and direct date evaluation to fix the errors
            if (booking.getEndDate() != null && booking.getCustomer() != null && booking.getTool() != null) {

                // Convert to string or check directly depending on the SQL type safety mapping
                String bookingEndDateStr = booking.getEndDate().toString();
                String tomorrowStr = tomorrow.toString();

                if (bookingEndDateStr.equals(tomorrowStr)) {
                    String customerEmail = booking.getCustomer().getEmail();
                    String customerName = booking.getCustomer().getName();
                    String toolName = booking.getTool().getName();

                    String message = "Hi " + customerName + ",\n\n" +
                            "This is a reminder that your rental tool [" + toolName +
                            "] is due for return tomorrow (" + tomorrow + ").\n\n" +
                            "Please return it on time to avoid any late fees!\n\n" +
                            "Best Regards,\nLankatools Team";

                    emailService.sendSimpleEmail(customerEmail, "Tool Return Reminder", message);
                    System.out.println("✉️ Sent reminder to: " + customerEmail + " for tool: " + toolName);
                }
            }
        }
        System.out.println("✅ Automated Scheduler Completed.");
    }
}