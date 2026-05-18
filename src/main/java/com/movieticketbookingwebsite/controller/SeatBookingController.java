package com.movieticketbookingwebsite.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieticketbookingwebsite.dto.BookingRequestDTO;
import com.movieticketbookingwebsite.dto.SeatStatusDTO;
import com.movieticketbookingwebsite.entity.Ticket;
import com.movieticketbookingwebsite.service.SeatBookingService;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "http://localhost:5173")
public class SeatBookingController {
	@Autowired
    private SeatBookingService bookingService;

    @GetMapping("/{id}/seats")
    public ResponseEntity<List<SeatStatusDTO>> getSeats(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getSeatsForShowTime(id));
    }
    
 // Tạo vé tạm thời (MỚI THÊM)
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDTO request, Principal principal) {
        try {
            // principal.getName() lấy username từ Token JWT
            Ticket ticket = bookingService.createPendingTicket(request, principal.getName());
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
