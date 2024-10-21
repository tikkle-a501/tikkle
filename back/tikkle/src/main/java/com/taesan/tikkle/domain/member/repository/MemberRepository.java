package com.taesan.tikkle.domain.member.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.member.dto.response.MemberRankProjection;
import com.taesan.tikkle.domain.member.dto.response.MemberRankResponse;
import com.taesan.tikkle.domain.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
	<T> Optional<T> findById(UUID id, Class<T> type);

	Optional<Member> findByEmail(String email);

	//1. jpql 조인 전체 데이터 + 레디스 sorted set
	@Query("SELECT new com.taesan.tikkle.domain.member.dto.response.MemberRankResponse(" +
		"m.id, m.name, a.rankingPoint, COUNT(tl.id)) " +
		"FROM Member m " +
		"JOIN Account a ON m.id = a.member.id " +
		"LEFT JOIN TradeLog tl ON a.id = tl.recAccount.id " +
		"GROUP BY m.id, m.nickname, a.rankingPoint")
	List<MemberRankResponse> findMemberRankings();

	//2. nativeQuery를 활용해 limit, order by 활용해 데이터 조회
	@Query(value = "SELECT m.id AS memberId, m.name AS nickname, a.ranking_point AS rankingPoint, COUNT(tl.id) AS tradeCount " +
		"FROM members m " +
		"JOIN accounts a ON m.id = a.member_id " +
		"LEFT JOIN trade_logs tl ON a.id = tl.rec_account_id " +
		"GROUP BY m.id, m.name, a.ranking_point " +
		"ORDER BY a.ranking_point DESC " +
		"LIMIT :limit OFFSET :offset",
		nativeQuery = true)
	List<MemberRankProjection> findMemberRankings(@Param("limit") int limit, @Param("offset") int offset);



	Optional<Member> findByIdAndDeletedAtIsNull(UUID memberId);
}
