import { MypageMemberResponses } from "@/types";
import { handleApiRequest } from "../client";

// 마이페이지
export const fetchMypageMember = async () => {
  return handleApiRequest<MypageMemberResponses, "get">("/member", "get");
};
