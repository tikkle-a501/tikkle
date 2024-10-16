package com.taesan.tikkle.domain.rank.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taesan.tikkle.domain.member.dto.response.MemberRankResponse;
import com.taesan.tikkle.domain.member.service.MemberService;
import com.taesan.tikkle.domain.rank.dto.response.RankBaseResponse;
import com.taesan.tikkle.domain.rank.dto.response.RankResponse;

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

	public RankBaseResponse getRankList(UUID username) {
		ZSetOperations<String, Object> stringObjectZSetOperations = redisTemplate.opsForZSet();
		Long existingCount = checkAndUpdateCache(stringObjectZSetOperations);

		List<MemberRankResponse> rankList = fetchAndSortRankList(stringObjectZSetOperations, existingCount);
		MemberRankResponse myRank = findMyRank(rankList, username);

		return RankResponse.of(rankList, myRank);
	}

	public RankBaseResponse getRankList(UUID username, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("ranking_point").descending());
		return RankResponse.of(memberService.findMemberRankings(pageable), null);
	}

	public RankBaseResponse getSearchRankList(String keyword) {
		ZSetOperations<String, Object> stringObjectZSetOperations = redisTemplate.opsForZSet();
		Long existingCount = checkAndUpdateCache(stringObjectZSetOperations);

		List<MemberRankResponse> rankList = fetchAndSortRankList(stringObjectZSetOperations, existingCount).stream()
			.filter(member -> member.getNickname().contains(keyword))
			.collect(Collectors.toList());

		return RankBaseResponse.from(rankList);
	}

	private Long checkAndUpdateCache(ZSetOperations<String, Object> stringObjectZSetOperations) {
		Long existingCount = stringObjectZSetOperations.size(RANKING_KEY);
		if (existingCount == null || existingCount == 0) {
			existingCount = updateCache(stringObjectZSetOperations);
		}
		return existingCount;
	}

	private List<MemberRankResponse> fetchAndSortRankList(ZSetOperations<String, Object> stringObjectZSetOperations,
		Long existingCount) {
		List<MemberRankResponse> rankList = Objects.requireNonNull(
				stringObjectZSetOperations.reverseRange(RANKING_KEY, 0, existingCount + 1))
			.stream()
			.map(this::convertToMemberRankResponse)
			.sorted((firstMember, secondMember) -> {
				int compareByPoint = Long.compare(secondMember.getRankingPoint(), firstMember.getRankingPoint());
				if (compareByPoint == 0) {
					return Long.compare(secondMember.getTradeCount(), firstMember.getTradeCount());
				}
				return compareByPoint;
			})
			.collect(Collectors.toList());
		grantRanksToMembers(rankList);
		return rankList;
	}

	private void grantRanksToMembers(List<MemberRankResponse> rankList) {
		int rankCounter = 1;
		for (int i = 0; i < rankList.size(); i++) {
			MemberRankResponse currentMember = rankList.get(i);
			if (i == 0 || !isTie(rankList.get(i - 1), currentMember)) {
				rankCounter = i + 1;
			}
			currentMember.grantOrder(rankCounter);
		}
	}

	private MemberRankResponse findMyRank(List<MemberRankResponse> rankList, UUID username) {
		for (MemberRankResponse member : rankList) {
			if (member.getMemberId().equals(username)) {
				return member;
			}
		}
		return null;
	}

	private boolean isTie(MemberRankResponse previousMember, MemberRankResponse currentMember) {
		return previousMember.getRankingPoint() == currentMember.getRankingPoint()
			&& previousMember.getTradeCount() == currentMember.getTradeCount();
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
