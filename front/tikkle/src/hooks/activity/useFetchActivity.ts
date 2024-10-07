import { useQuery } from "@tanstack/react-query";
import { ActivityGetResponses } from "@/types";
import { fetchActivity } from "@/libs";

export const useFetchActivity = () => {
  return useQuery<ActivityGetResponses, Error>({
    queryKey: ["Activity"],
    queryFn: fetchActivity,
  });
};
