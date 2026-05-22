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

    List<Booking> findByCustomer(User customer);

    List<Booking> findByToolOwner(User owner);

    boolean existsByToolAndStatusNotInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Tool tool,
            List<BookingStatus> excludedStatuses,
            LocalDate endDate,
            LocalDate startDate
    );
}