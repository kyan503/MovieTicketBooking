package com.movieticketbookingwebsite.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;



@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// Bộ điều phối đẩy tin nhắn từ Server xuống Client qua các kênh bắt đầu bằng /topic
		config.enableSimpleBroker("/topic");
		// Tiền tố nhận lệnh xử lý dữ liệu từ Client gửi lên Server
		config.setApplicationDestinationPrefixes("/app");
		
		
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// Điểm bắt đầu kết nối (Handshake) giữa React và Spring Boot
		registry.addEndpoint("/ws")
		         .setAllowedOrigins("http://localhost:5173")
		         .withSockJS();
		
		
	}
	
	
}
