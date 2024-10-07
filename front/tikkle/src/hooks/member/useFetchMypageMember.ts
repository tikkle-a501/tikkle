import { useQuery } from "@tanstack/react-query";
import { MypageMemberResponse } from "@/types";
import { fetchMypageMember } from "@/libs";

export const useFetchMypageMember = () => {
  return useQuery<MypageMemberResponse, Error>({
    queryKey: ["mypageMember"],
    queryFn: () => fetchMypageMember(),
  });
};
