package com.movieticketbookingwebsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movieticketbookingwebsite.dto.JwtResponse;
import com.movieticketbookingwebsite.dto.LoginRequest;
import com.movieticketbookingwebsite.dto.RegisterRequest;
import com.movieticketbookingwebsite.entity.User;
import com.movieticketbookingwebsite.repository.UserRepository;
import com.movieticketbookingwebsite.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Spring sẽ tự hiểu là BCrypt do bạn cấu hình ở SecurityConfig

    @Autowired
    private JwtTokenProvider tokenProvider;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Xác thực username và password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Nếu không lỗi, tạo JWT và trả về cho client
        String jwt = tokenProvider.generateToken((UserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
    	if(userRepository.existsByUsername(registerRequest.getUsername())) {
    		return ResponseEntity.badRequest().body("Error: Username is already taken!");
    	
    	}
    	User user = new User();
    	user.setUsername(registerRequest.getUsername());
    	user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    	user.setEmail(registerRequest.getEmail());
    	user.setRole("ROLL_USER");
    	userRepository.save(user);
    	return ResponseEntity.ok("User đã đăng ký thành công");
    }
}