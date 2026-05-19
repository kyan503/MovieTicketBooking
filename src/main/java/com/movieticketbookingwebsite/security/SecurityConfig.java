package com.movieticketbookingwebsite.security;



import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Dòng này cực kỳ quan trọng

@Configuration // sử dụng phương thức Bean để đăng ký các cài đặt
@EnableWebSecurity // kích hoạt Spring Security
public class SecurityConfig {

	    @Autowired
	    private CustomUserDetailsService userDetailsService;

	    // 1. Khai báo Bean mã hóa mật khẩu (Bắt buộc)
	    @Bean
	   public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    // 2. Khai báo Bean quản lý xác thực
	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	        return authConfig.getAuthenticationManager();
	    }

	    // 3. Khai báo Filter kiểm tra Token
	    @Bean
	    public JwtAuthenticationFilter jwtAuthenticationFilter() {
	        return new JwtAuthenticationFilter();
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        return http
	                .csrf(csrf -> csrf.disable())
	                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .authorizeHttpRequests(auth -> auth
	                        // SỬA TẠI ĐÂY: Đảm bảo có dấu / và thử dùng authorizeHttpRequests chuẩn
	                        .requestMatchers("/api/auth/**",
	                        		"/error",
	                        		"/api/payment/vnpay-callback**",
	                        		"/ws/**").permitAll()     
	                        .anyRequest().authenticated()
	                )
	                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
	                .build();
	    }

	    // Thêm Bean này vào trong SecurityConfig để Postman và React gọi được API
	    @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Cho phép tất cả các nguồn
	        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
	        configuration.setAllowCredentials(true);
	        
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }
	    
	
	}
