import { ActivityGetResponses } from "@/types";
import { handleApiRequest } from "../client";

// 나의 게시글 조회
export const fetchActivity = async () => {
  return handleApiRequest<ActivityGetResponses, "get">("/member/boards", "get");
};
