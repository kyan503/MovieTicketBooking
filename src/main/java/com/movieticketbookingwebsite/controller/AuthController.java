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
        // 1. Xác thực username và password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 2. Lấy thông tin UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Tạo JWT
        String jwt = tokenProvider.generateToken(userDetails);

        // 4. Lấy Role (Giả sử User chỉ có 1 Role)
        String role = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .findFirst()
                .orElse("ROLE_USER");

        // 5. TRẢ VỀ ĐẦY ĐỦ THÔNG TIN CHO FRONTEND
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), role));
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
    	user.setRole("ROLE_USER");
    	userRepository.save(user);
    	return ResponseEntity.ok("User đã đăng ký thành công");
    }
}