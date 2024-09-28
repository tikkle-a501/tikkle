package com.taesan.tikkle.domain.rank.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taesan.tikkle.domain.member.dto.response.MemberRankResponse;
import com.taesan.tikkle.domain.member.service.MemberService;
import com.taesan.tikkle.domain.rank.dto.RankResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final MemberService memberService;
	private final ObjectMapper objectMapper;

	private static final String RANKING_KEY = "ranking";

	public RankResponse getRankList(UUID username) {
		ZSetOperations<String, Object> stringObjectZSetOperations = redisTemplate.opsForZSet();
		Long existingCount = stringObjectZSetOperations.size(RANKING_KEY);

		if (isNotCache(existingCount)) {
			existingCount = updateCache(stringObjectZSetOperations);
		}

		AtomicReference<MemberRankResponse> myRankRef = new AtomicReference<>();
		AtomicInteger rankCounter = new AtomicInteger(1);

		List<MemberRankResponse> rankList = Objects.requireNonNull(
				stringObjectZSetOperations.reverseRange(RANKING_KEY, 1, existingCount + 1))
			.stream()
			.map(this::convertToMemberRankResponse)
			.peek(memberRank -> {
				memberRank.grantOrder(rankCounter.getAndIncrement());
				if (memberRank.getMemberId().equals(username)) {
					myRankRef.set(memberRank);
				}
			})
			.toList();

		MemberRankResponse myRank = myRankRef.get();

		return RankResponse.of(rankList, myRank);
	}

	private boolean isNotCache(Long existingCount) {
		return existingCount == null || existingCount == 0;
	}

	private Long updateCache(ZSetOperations<String, Object> stringObjectZSetOperations) {
		memberService.findMemberRankings()
			.forEach(
				memberRanking -> stringObjectZSetOperations.add(
					RANKING_KEY,
					memberRanking,
					memberRanking.getRankingPoint()));
		return stringObjectZSetOperations.size(RANKING_KEY);
	}

	@Scheduled(cron = "0 1 * * * *")
	public void deleteCache() {
		redisTemplate.delete(RANKING_KEY);
		log.info("Redis 랭킹 삭제");
		ZSetOperations<String, Object> stringObjectZSetOperations = redisTemplate.opsForZSet();
		updateCache(stringObjectZSetOperations);
		log.info("Redis 랭킹 업데이트");
	}

	private MemberRankResponse convertToMemberRankResponse(Object data) {
		return objectMapper.convertValue(data, MemberRankResponse.class);
	}
}
