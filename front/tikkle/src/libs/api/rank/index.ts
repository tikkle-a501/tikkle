import { RankGetResponses, RankSearchGetResponses } from "@/types";
import { handleApiRequest } from "../client";

// 랭킹 조회
export const fetchRank = async () => {
  return handleApiRequest<RankGetResponses, "get">("/rank", "get");
};

// 랭킹 검색
export const searchRank = async (memberId: string) => {
  return handleApiRequest<RankSearchGetResponses, "get">(
    `/rank/${memberId}`,
    "get",
  );
};
