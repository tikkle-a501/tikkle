// 리뷰 작성 요청
export interface ReviewCreateRequest {
  type: string;
  content: string;
}

// 리뷰 조회 응답
export interface ReviewGetResponse {
  type: string;
  content: string;
}

export interface ReviewGetResponses {
  data: ReviewGetResponse[];
}