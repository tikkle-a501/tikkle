// 환율 조회 응답
export interface RateGetResponse {
  rateId: string;
  createdAt: string;
  timeToRank: number;
}

export interface RateGetResponses {
  data: RateGetResponse[];
}
