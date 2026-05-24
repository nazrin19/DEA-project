package com.example.Lankatools.controller;

import com.example.Lankatools.entity.Booking;
import com.example.Lankatools.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class ExportController {

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * GET /api/admin/export/bookings
     * Downloads all bookings as a CSV file — Admin only.
     */
    @GetMapping("/export/bookings")
    public ResponseEntity<byte[]> exportBookingsCSV() throws Exception {

        List<Booking> bookings = bookingRepository.findAll();
        byte[] csvBytes;
        
        // Try-with-resources handles auto-flushing and closing gracefully
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(out, false, StandardCharsets.UTF_8)) {

            // CSV Header row
            writer.println("Booking ID,Customer Name,Customer Email,Customer Phone,Tool Name,Tool Category,Daily Rate (LKR),Start Date,End Date,Total Cost (LKR),Status");

            // CSV Data rows mapped directly to your Entity fields
            for (Booking b : bookings) {
                writer.printf("%d,%s,%s,%s,%s,%s,%.2f,%s,%s,%.2f,%s%n",
                    b.getId(),
                    escapeCsv(b.getCustomer() != null ? b.getCustomer().getName() : ""),
                    escapeCsv(b.getCustomer() != null ? b.getCustomer().getEmail() : ""),
                    escapeCsv(b.getCustomer() != null ? b.getCustomer().getPhone() : ""),
                    escapeCsv(b.getTool() != null ? b.getTool().getName() : ""),
                    escapeCsv(b.getTool() != null ? b.getTool().getCategory() : ""),
                    b.getTool() != null ? b.getTool().getDailyRate() : 0.0,
                    b.getStartDate() != null ? b.getStartDate().toString() : "",
                    b.getEndDate() != null ? b.getEndDate().toString() : "",
                    b.getTotalCost(), // primitive double, safe from null
                    b.getStatus() != null ? b.getStatus().toString() : ""
                );
            }
            
            writer.flush();
            csvBytes = out.toByteArray(); // Safely extracted before streams close
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"bookings-export.csv\"")
            .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
            .contentLength(csvBytes.length)
            .body(csvBytes);
    }

    // Handles escaping for names, shop addresses, or descriptions containing commas/quotes
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}