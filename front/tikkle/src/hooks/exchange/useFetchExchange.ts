import { useQuery } from "@tanstack/react-query";
import { ExchangeGetResponses } from "@/types";
import { fetchExchange } from "@/libs";

export const useFetchExchange = () => {
  return useQuery<ExchangeGetResponses, Error>({
    queryKey: ["exchange"],
    queryFn: () => fetchExchange(),
  });
};
