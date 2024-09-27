// 환전 요청
export interface ExchangeCreateRequest {
  rateId: string;
  timeToRank: number;
  quantity: number;
  exchangeType: string;
}

// 환전 내역 조회
export interface ExchangeGetResponse {
  time: number;
  rankingPoint: number;
  createdAt: stringe;
  exchangeType: string;
}

export interface ExchangeGetResponses {
  data: ExchangeGetResponse[];
}
