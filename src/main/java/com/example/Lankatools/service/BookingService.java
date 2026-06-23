package com.example.Lankatools.service;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.entity.User;
import com.example.Lankatools.enums.BookingStatus;
import com.example.Lankatools.enums.Toolstatus;
import com.example.Lankatools.repository.BookingRepository;
import com.example.Lankatools.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private UserService userService; // Injected to retrieve the User entity by email

    @Autowired
    private EmailService emailService;

    /**
     * Creates a tool booking after validating overlap checks, updates database, and emails confirmation.
     */
    public Booking createBooking(String customerEmail, Long toolId, LocalDate startDate, LocalDate endDate) {

        // 1. Fetch the Tool entity
        Tool tool = toolRepository.findById(toolId)
                .orElseThrow(() -> new IllegalArgumentException("Tool not found."));

        // 2. Fetch the User entity using the email context
        User customer = userService.getUserByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("User account not found for email: " + customerEmail);
        }

        // 3. Simple date sequence validation
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be the same or after the start date.");
        }

        // 4. Match the exact Boolean check method written in your BookingRepository
        // Exclude cancelled/rejected orders from blocking new bookings
        List<BookingStatus> excludedStatuses = Arrays.asList(BookingStatus.REJECTED, BookingStatus.CANCELLED);

        boolean hasOverlap = bookingRepository.existsByToolAndStatusNotInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                tool,
                excludedStatuses,
                endDate,
                startDate
        );

        if (hasOverlap) {
            throw new IllegalArgumentException("Selected dates overlap with an active booking for this tool.");
        }

        // 5. Calculate final rental cost metrics
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double totalCost = days * tool.getDailyRate();

        // 6. Assemble the clean Booking object mappings
        Booking booking = new Booking();
        booking.setTool(tool);
        booking.setCustomer(customer);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalCost(totalCost);

        // 7. Write to MySQL database
        Booking savedBooking = bookingRepository.save(booking);
        System.out.println("🚀 Success: Booking record stored cleanly for " + tool.getName());

        // 8. Safely pass to the email engine
        try {
            sendConfirmationEmail(savedBooking);
        } catch (Exception e) {
            System.err.println("⚠️ Email failed to send, but data was saved safely: " + e.getMessage());
        }

        return savedBooking;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsForCustomer(String email) {
        User customer = userService.getUserByEmail(email);
        return bookingRepository.findByCustomer(customer);
    }

    public List<Booking> getBookingsForToolOwner(String email) {
        User owner = userService.getUserByEmail(email);
        return bookingRepository.findByTool_Owner(owner);
    }

    public List<Tool> getApprovedTools() {
        return toolRepository.findByStatus(Toolstatus.APPROVED);
    }

    // --- State lifecycle mapping hooks ---

    public Booking confirmBooking(Long id, String username) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking reference not found."));
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    public Booking rejectBooking(Long id, String username) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking reference not found."));
        booking.setStatus(BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }

    public Booking cancelBooking(Long id, String username) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking reference not found."));
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public Booking markReturned(Long id, String username) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking reference not found."));
        booking.setStatus(BookingStatus.RETURNED);
        return bookingRepository.save(booking);
    }

    /**
     * Dispatcher helper utilizing your internal EmailService layer
     */
    private void sendConfirmationEmail(Booking booking) {
        String subject = "🛠️ Lankatools - Booking Confirmation!";

        // This is 100% correct because Lombok generates getName() from 'private String name;'
        String body = "Dear " + booking.getCustomer().getName() + ",\n\n" +
                "Your booking request for the tool '" + booking.getTool().getName() + "' has been filed as PENDING.\n" +
                "Duration: " + booking.getStartDate() + " to " + booking.getEndDate() + "\n" +
                "Estimated Total: $" + booking.getTotalCost() + "\n\n" +
                "Thank you for choosing Lankatools.";

        try {
            // This is 100% correct because Lombok generates getEmail() from 'private String email;'
            emailService.sendSimpleEmail(booking.getCustomer().getEmail(), subject, body);
            System.out.println("📬 Notification dispatched to user checkout mailbox.");
        } catch (Exception e) {
            System.err.println("❌ Could not send email: " + e.getMessage());
        }
    }
}