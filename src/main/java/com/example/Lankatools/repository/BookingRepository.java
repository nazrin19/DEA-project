package com.example.Lankatools.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.enums.Bookingstatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
           "WHERE b.tool.id = :toolId " +
           "AND b.status IN :statuses " +
           "AND b.startDate <= :endDate " +
           "AND b.endDate >= :startDate")
    List<Booking> findOverlappingBookings(
            @Param("toolId") Long toolId,
            @Param("statuses") List<Bookingstatus> statuses,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
