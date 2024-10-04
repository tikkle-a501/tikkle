import { fetchBoardByKeyword, fetchBoardList } from "@/libs";
import { BoardListResponses } from "@/types/board";
import { useQuery } from "@tanstack/react-query";

export const useFetchBoardByKeyword = (keyword: string) => {
  return useQuery<BoardListResponses, Error>({
    queryKey: ["Boards", keyword],
    queryFn: () => fetchBoardByKeyword(keyword),
  });
};
