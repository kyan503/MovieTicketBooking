package com.movieticketbookingwebsite.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieticketbookingwebsite.entity.User;
import com.movieticketbookingwebsite.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
("http://localhost:5173")
public class UserController {
	
	@Autowired
    private UserRepository userRepository;
	
	@GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Principal principal) {
		if (principal == null) {
            return ResponseEntity.status(401).body("Bạn chưa đăng nhập!");
        }
		
		User user = userRepository.findByUsername(principal.getName());
		if (user != null) {
			Map<String,Object> profile = new HashMap<>();
			profile.put("id", user.getId());
			profile.put("username", user.getUsername());
			profile.put("email", user.getEmail());
			return ResponseEntity.ok(profile);
		} else {
			
			return ResponseEntity.status(404).body("Không tìm thấy thông tin người dùng!");
		}
		
		
	}

}
