import { useQuery } from "@tanstack/react-query";
import { ReviewGetResponses } from "@/types";
import { fetchReview } from "@/libs";

export const useFetchReview = () => {
  return useQuery<ReviewGetResponses, Error>({
    queryKey: ["review"],
    queryFn: () => fetchReview(),
  });
};
