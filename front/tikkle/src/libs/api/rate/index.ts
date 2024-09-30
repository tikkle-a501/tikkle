import { RateGetResponses } from "@/types";
import { handleApiRequest } from "../client";

export const fetchRate = async () => {
  return handleApiRequest<RateGetResponses, "get">("/rate", "get");
};

// 새로운 환율 데이터를 생성하는 API 요청 함수(테스트용으로만 쓰임)
export const createRate = async () => {
  return handleApiRequest<void, "post">("/rate", "post");
};
