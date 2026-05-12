package com.movieticketbookingwebsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieticketbookingwebsite.dto.ShowtimeDTO;
import com.movieticketbookingwebsite.service.ShowtimeService;

@RestController
@RequestMapping("/api/showtimes")
@CrossOrigin("*")
public class ShowtimeController {
	@Autowired
    private ShowtimeService showtimeService;

    @GetMapping
    public ResponseEntity<List<ShowtimeDTO>> getAll() {
        return ResponseEntity.ok(showtimeService.getAllShowtimes());
    }
}
