import { useQuery } from "@tanstack/react-query";
import { AccountGetResponses } from "@/types";
import { fetchAccount } from "@/libs";

export const useFetchAccount = () => {
  return useQuery<AccountGetResponses, Error>({
    queryKey: ["account"],
    queryFn: () => fetchAccount(),
  });
};
