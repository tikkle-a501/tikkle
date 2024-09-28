package com.taesan.tikkle.domain.rank.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taesan.tikkle.domain.member.dto.response.MemberRankResponse;
import com.taesan.tikkle.domain.member.service.MemberService;

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

	public List<MemberRankResponse> getRankList() {
		ZSetOperations<String, Object> stringObjectZSetOperations = redisTemplate.opsForZSet();
		Long existingCount = stringObjectZSetOperations.size(RANKING_KEY);

		if (isNotCache(existingCount)) {
			existingCount = makeRankList(stringObjectZSetOperations);
		}
		return Objects.requireNonNull(stringObjectZSetOperations.reverseRange(RANKING_KEY, 1, existingCount + 1))
			.stream()
			.map(this::convertToMemberRankResponse)
			.collect(Collectors.toList());
	}

	private boolean isNotCache(Long existingCount) {
		return existingCount == null || existingCount == 0;
	}

	private Long makeRankList(ZSetOperations<String, Object> stringObjectZSetOperations) {
		memberService.findMemberRankings()
			.forEach(
				memberRanking -> stringObjectZSetOperations.add(
					RANKING_KEY,
					memberRanking,
					memberRanking.getRankingPoint()));
		return stringObjectZSetOperations.size(RANKING_KEY);
	}

	// @Scheduled(cron = "0/1 * * * * *")
	public void deleteRankList() {
		redisTemplate.delete(RANKING_KEY);
		log.info("기존 랭킹 데이터 삭제 완료");
	}
	private MemberRankResponse convertToMemberRankResponse(Object data) {
		return objectMapper.convertValue(data, MemberRankResponse.class);
	}
}
