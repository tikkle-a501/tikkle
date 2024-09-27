import { useQuery } from "@tanstack/react-query";
import { TradeSearchResponses } from "@/types";
import { searchTrade } from "@/libs";

export const useSearchTrade = (keyword: string) => {
  return useQuery<TradeSearchResponses, Error>({
    queryKey: ["trade", keyword],
    queryFn: () => searchTrade(keyword),
    enabled: !!keyword,
  });
};
