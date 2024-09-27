import { useMutation } from "@tanstack/react-query";
import { handleSuccess, handleError } from "@/libs/utils";
import { ReviewCreateRequest } from "@/types";
import { createReview } from "@/libs";
import { error } from "console";

export const useCreateReview = (data: ReviewCreateRequest) => {
  return useMutation<void, Error, { data: ReviewCreateRequest }>({
    mutationFn: () => createReview(data),
    onSuccess: (data) => {
      handleSuccess("✅ Review created successfully", data);
    },
    onError: (error: Error) => {
      handleError("😥 Failed to create review", error);
    },
  });
};
