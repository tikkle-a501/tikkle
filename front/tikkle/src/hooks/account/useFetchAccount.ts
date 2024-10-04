import { useQuery } from "@tanstack/react-query";
import { AccountGetResponses } from "@/types";
import { fetchAccount } from "@/libs";

export const useFetchAccount = () => {
  return useQuery<AccountGetResponses, Error>({
    refetchInterval: false,
    retry: false,
    queryKey: ["account"],
    queryFn: () => fetchAccount(),
  });
};
