import { useQuery } from "@tanstack/react-query";
import { RankSearchGetResponses } from "@/types";
import { searchRank } from "@/libs";

export const useSearchRank = (memberId: string) => {
  return useQuery<RankSearchGetResponses, Error>({
    queryKey: ["rank", memberId],
    queryFn: () => searchRank(memberId),
  });
};
