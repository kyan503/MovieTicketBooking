package com.movieticketbookingwebsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieticketbookingwebsite.dto.SeatStatusDTO;
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
}
