import { useQuery } from "@tanstack/react-query";
import { RateGetResponses } from "@/types";
import { fetchRate } from "@/libs";

// 환율 리스트(차트) 조회
export const useFetchRate = () => {
  return useQuery<RateGetResponses, Error>({
    queryKey: ["rate"],
    queryFn: () => fetchRate(),
  });
};
