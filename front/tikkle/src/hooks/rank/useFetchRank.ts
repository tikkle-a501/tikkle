import { useQuery } from "@tanstack/react-query";
import { RankGetResponses } from "@/types";
import { fetchRank } from "@/libs";

export const useFetchRank = () => {
  return useQuery<RankGetResponses, Error>({
    queryKey: ["rank"],
    queryFn: () => fetchRank(),
  });
};
