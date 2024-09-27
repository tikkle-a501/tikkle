import { ExchangeCreateRequest, ExchangeGetResponses } from "@/types";
import { handleApiRequest } from "../client";

// 환전 요청
export const createExchange = async (data: ExchangeCreateRequest) => {
  return handleApiRequest<void, "post", ExchangeCreateRequest>(
    "/exchange",
    "post",
    data,
  );
};

// 환전 내역 조회
export const fetchExchange = async () => {
  return handleApiRequest<ExchangeGetResponses, "get">("/exchange", "get");
};
