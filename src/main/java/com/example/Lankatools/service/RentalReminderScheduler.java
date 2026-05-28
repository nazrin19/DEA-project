package com.example.Lankatools.service;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import java.time.LocalDate;
import java.util.List;

@Component
public class RentalReminderScheduler {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendReturnReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        System.out.println("Automated Scheduler Woke Up! Checking for bookings ending on: " + tomorrow);

        List<Booking> allBookings = bookingRepository.findAll();

        for (Booking booking : allBookings) {
            if (booking.getEndDate() != null && booking.getCustomer() != null && booking.getTool() != null) {

                String bookingEndDateStr = booking.getEndDate().toString();
                String tomorrowStr = tomorrow.toString();

                if (bookingEndDateStr.equals(tomorrowStr)) {
                    String customerEmail = booking.getCustomer().getEmail();

                    Context context = new Context();
                    context.setVariable("customerName", booking.getCustomer().getName());
                    context.setVariable("toolName", booking.getTool().getName());
                    context.setVariable("dueDate", tomorrow.toString());

                    emailService.sendHtmlTemplateEmail(
                            customerEmail,
                            "Tool Return Reminder: Due Tomorrow!",
                            context,
                            "rental-reminder-email"
                    );
                }
            }
        }
        System.out.println("Automated Scheduler Completed.");
    }
}