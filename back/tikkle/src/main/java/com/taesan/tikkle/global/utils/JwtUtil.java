package com.taesan.tikkle.global.utils;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private final String JWT_KEY = System.getenv("JWT_KEY");

	public String generateToken(String username) {
		return Jwts.builder()
			.setSubject(username)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
			.signWith(SignatureAlgorithm.HS256, JWT_KEY)
			.compact();
	}

	public String generateToken(Map<String, Object> claims, String username) {
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(username)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
			.signWith(SignatureAlgorithm.HS256, JWT_KEY)
			.compact();
	}

	public String generateRefreshToken(Map<String, Object> claims, String username) {
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(username)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
			.signWith(SignatureAlgorithm.HS256, JWT_KEY)
			.compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public boolean validateToken(String token, String username) {
		final String extractedUsername = extractUsername(token);
		return (extractedUsername.equals(username) && !isTokenExpired(token));
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(JWT_KEY)
			.parseClaimsJws(token).getBody();
	}

	private boolean isTokenExpired(String token) {
		return extractAllClaims(token).getExpiration().before(new Date());
	}

	public boolean isHalfLifePassed(String refreshToken) {
		try {
			// JWT 토큰을 파싱하여 클레임을 가져옴
			Claims claims = Jwts.parser()
				.setSigningKey(JWT_KEY)
				.parseClaimsJws(refreshToken)
				.getBody();

			// 만료 시간(exp)과 발급 시간(iat)을 가져옴
			Date expiration = claims.getExpiration();  // 만료 시간
			Date issuedAt = claims.getIssuedAt();      // 발급 시간

			// 만료 시간과 발급 시간 사이의 절반 시점을 계산
			long halfLifeTime = issuedAt.getTime() + ((expiration.getTime() - issuedAt.getTime()) / 2);

			// 현재 시각이 유효기간의 절반을 넘었는지 확인
			return new Date().getTime() > halfLifeTime;

		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}
