import { useQuery } from "@tanstack/react-query";
import { MypageMemberResponses } from "@/types";
import { fetchMypageMember } from "@/libs";

export const useFetchMypageMember = () => {
  return useQuery<MypageMemberResponses, Error>({
    queryKey: ["mypageMember"],
    queryFn: () => fetchMypageMember(),
  });
};
