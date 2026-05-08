package com.movieticketbookingwebsite.security;



import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	private final String JWT_SECRET = "chuoi_bi_mat_rat_dai_va_bao_mat_cua_ban";
	private final long JWT_EXPIRATION = 604800000L;
	
	// Tạo ra token từ thông tin user
	public String generateToken(UserDetails userDetails) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
		
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, JWT_SECRET)
				.compact();
		
	}
	// Lấy username từ token đã mã hóa
	public String getUsernameFromJWT(String token) {
	   Claims claims = Jwts.parser()
			           .setSigningKey(JWT_SECRET)
			           .parseClaimsJws(token)
			           .getBody();
	   return claims.getSubject();
			
	}
	// Kiểm tra xem token có hợp lệ hay không
	public Boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
}
