import {
  AccountGetResponses,
  TradeSearchResponses,
  TradeGetResponses,
} from "@/types";
import { handleApiRequest } from "../client";

// 계좌 조회
export const fetchAccount = async () => {
  return handleApiRequest<AccountGetResponses, "get">("/account", "get");
};

// 거래내역 조회
export const fetchTrade = async () => {
  return handleApiRequest<TradeGetResponses, "get">("/trade", "get");
};

// 거래내역 검색
export const searchTrade = async (keyword: string) => {
  return handleApiRequest<TradeSearchResponses, "get">(
    `/account/search?keyword=${keyword}`,
    "get",
  );
};
