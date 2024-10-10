// 계좌 조회
export interface AccountGetResponse {
  accountId: string;
  timeQnt: number;
  rankingPoint: number;
}

// 거래 내역 조회
export interface TradeMemberGetResponse {
  id: string;
  name: string;
}

export interface TradeGetResponses {
  member: TradeMemberGetResponse;
  partnerImage: string;
  title: string;
  content: string;
  time: number;
  status: string;
  createdAt: string;
}

export interface TrageGetResponses {
  data: TradeGetResponse[];
}

// 거래 내역 검색
export interface TradeSearchResponses {
  data: TradeGetResponse[];
}
