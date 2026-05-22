package com.example.Lankatools.repository;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.entity.Tool;
import com.example.Lankatools.entity.User;
import com.example.Lankatools.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // --- Core Engine Methods (Your Branch) ---
    List<Booking> findByCustomer(User customer);

    List<Booking> findByToolOwner(User owner);

    boolean existsByToolAndStatusNotInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Tool tool,
            List<BookingStatus> excludedStatuses,
            LocalDate endDate,
            LocalDate startDate
    );

    // --- Combined Notification & Automated Job Methods (Teammates' Tasks) ---
    // Fixed: Changed String status to proper BookingStatus enum type
    List<Booking> findByEndDateAndStatus(LocalDate endDate, BookingStatus status);

    // Counts how many bookings are waiting for approval (for the navbar badge counter)
    long countByStatus(BookingStatus status);

    // Fetches the actual pending bookings to display in a list on the screen
    List<Booking> findByStatus(BookingStatus status);
}
