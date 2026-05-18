package com.example.Lankatools.repository;

import com.example.Lankatools.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // This custom query method is exactly what your automated job needs to run!
    List<Booking> findByEndDateAndStatus(LocalDate endDate, String status);
}