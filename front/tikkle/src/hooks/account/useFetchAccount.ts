import { useQuery } from "@tanstack/react-query";
import { AccountGetResponse } from "@/types";
import { fetchAccount } from "@/libs";

export const useFetchAccount = () => {
  return useQuery<AccountGetResponse, Error>({
    refetchInterval: false,
    retry: false,
    queryKey: ["account"],
    queryFn: () => fetchAccount(),
  });
};
