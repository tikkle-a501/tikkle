package com.taesan.tikkle.domain.member.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTokenService {

	private final RedisTemplate<String, String> redisTemplate;

	public RedisTokenService(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void storeRefreshToken(UUID userId, String refreshToken, long expirationTime) {
		redisTemplate.opsForValue().set(userId.toString(), refreshToken, expirationTime, TimeUnit.MILLISECONDS);
	}

	public String getRefreshToken(String userId) {
		return redisTemplate.opsForValue().get(userId);
	}

	public void deleteRefreshToken(String userId) {
		redisTemplate.delete(userId);
	}
}