import { RankGetResponses, RankSearchGetResponses } from "@/types";
import { handleApiRequest } from "../client";

// 랭킹 조회
export const fetchRank = async () => {
  return handleApiRequest<RankGetResponses, "get">("/rank", "get");
};

// 랭킹 검색 (keyword 사용)
export const searchRank = async (keyword: string) => {
  return handleApiRequest<RankSearchGetResponses, "get">(
    `/api/v1/rank/search?keyword=${encodeURIComponent(keyword)}`,
    // 특수 문자가 포함될 수 있으므로 encodeURIComponent 함수를 사용해 안전하게 URL 인코딩을 적용
    "get",
  );
};
