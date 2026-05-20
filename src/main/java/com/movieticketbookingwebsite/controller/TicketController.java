package com.movieticketbookingwebsite.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieticketbookingwebsite.entity.Ticket;
import com.movieticketbookingwebsite.entity.User;
import com.movieticketbookingwebsite.repository.UserRepository;
import com.movieticketbookingwebsite.service.TicketService;

@RestController
@RequestMapping("api/tickets")
@CrossOrigin("http://localhost:5173")
public class TicketController {

	@Autowired
    private TicketService ticketService;

    @Autowired
    private UserRepository userRepository;
    
	@GetMapping("/history")
	public ResponseEntity<?> getBookingHistory(Principal principal) {
		if (principal == null) {
			return ResponseEntity.status(401).body("Bạn chưa đăng nhập");

		}
		User user = userRepository.findByUsername(principal.getName());

		if (user != null) {
			List<Ticket> history = ticketService.getBookingHistory(user);
			return ResponseEntity.ok(history);
		} else {
			return ResponseEntity.status(404).body("Không tìm thấy thông tin tài khoản!");

		}

	}

}
