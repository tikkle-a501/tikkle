// 환율 조회 응답
export interface RateGetResponse {
  id: string;
  createdAt: string;
  timeToRate: string;
}

export interface RateGetResponses {
  data: RateGetResponse[];
}
