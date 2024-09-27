import { ReviewCreateRequest, ReviewGetResponses } from "@/types";
import { handleApiRequest } from "../client";

// 리뷰 작성
export const createReview = async (data: ReviewCreateRequest) => {
  return handleApiRequest<void, "post", ReviewCreateRequest>(
    "./review",
    "post",
    data,
  );
};

// 리뷰 조회
export const fetchReview = async () => {
  return handleApiRequest<ReviewGetResponses, "get">("/review", "get");
};
