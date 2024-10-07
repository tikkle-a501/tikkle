import { useMutation } from "@tanstack/react-query";
import { handleSuccess, handleError } from "@/libs/utils";
import { ReviewCreateRequest } from "@/types";
import { createReview } from "@/libs";

export const useCreateReview = (data: ReviewCreateRequest) => {
  return useMutation<any, Error, ReviewCreateRequest>({
    mutationFn: (reviewData: ReviewCreateRequest) => createReview(reviewData),
    onSuccess: (data) => {
      handleSuccess("✅ Review created successfully", data);
    },
    onError: (error: Error) => {
      handleError("😥 Failed to create review", error);
    },
  });
};
