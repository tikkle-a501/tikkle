import { useQuery } from "@tanstack/react-query";
import { RankSearchGetResponses } from "@/types";
import { searchRank } from "@/libs";

export const useSearchRank = (keyword: string) => {
  return useQuery<RankSearchGetResponses, Error>({
    queryKey: ["rankSearch", keyword],
    queryFn: () => searchRank(keyword),
    enabled: !!keyword, // keyword가 있을 때만 쿼리 실행
  });
};
