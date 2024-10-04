// 랭킹 조회 응답
export interface RankGetResponse {
  order: number;
  memberId: string;
  nickname: string;
  rankingPoint: number;
  tradeCount: number;
}

export interface RankGetResponses {
  rankList: RankGetResponse[];
  myRank: RankGetResponse;
}

// 랭킹 검색 응답
export interface RankSearchGetResponses {
  rankList: RankGetResponse[];
}
