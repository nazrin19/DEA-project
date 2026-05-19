package com.example.Lankatools.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.enums.Bookingstatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByToolIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long toolId,
            List<Bookingstatus> statuses,
            LocalDate endDate,
            LocalDate startDate
    );
}
