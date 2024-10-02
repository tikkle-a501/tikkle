import { useQuery } from "@tanstack/react-query";
import { TradeGetResponses } from "@/types";
import { fetchTrade } from "@/libs";

export const useFetchTrade = () => {
  return useQuery<TradeGetResponses, Error>({
    queryKey: ["Trade"],
    queryFn: fetchTrade,
  });
};
