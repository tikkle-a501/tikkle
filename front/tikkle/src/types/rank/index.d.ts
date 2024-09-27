// 랭킹 조회 응답
export interface RankGetResponse {
  order: number;
  memberId: string;
  nickname: string;
  rankingPoint: number;
}

export interface RankGetResponses {
  data: RankGetResponse[];
  myrRank: RankGetResponse[];
}

// 랭킹 검색 응답
export interface RankSearchGetResponses {
  data: RankGetResponse[];
}
