import { useQuery } from "@tanstack/react-query";
import { RateGetResponses } from "@/types";
import { fetchRate } from "@/libs";

export const useFetchRate = () => {
  return useQuery<RateGetResponses, Error>({
    queryKey: ["Rate"],
    queryFn: () => fetchRate(),
  });
};
