import { useQuery } from "@tanstack/react-query";
import { RateGetResponse } from "@/types";
import { fetchRecentRate } from "@/libs";

export const useFetchRecentRate = () => {
  return useQuery<RateGetResponse, Error>({
    queryKey: ["recent-rate"],
    queryFn: () => fetchRecentRate(),
  });
};
