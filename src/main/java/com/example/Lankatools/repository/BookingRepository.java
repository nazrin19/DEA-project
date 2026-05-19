package com.example.Lankatools.repository;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.enums.Bookingstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByToolIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long toolId,
            List<Bookingstatus> statuses,
            LocalDate endDate,
            LocalDate startDate
    );
}
