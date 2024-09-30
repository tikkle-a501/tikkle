import { fetchBoardList } from "@/libs";
import { BoardListResponses, Board } from "@/types/board";
import { useQuery } from "@tanstack/react-query";

export const useFetchBoardList = () => {
  return useQuery<Board[], Error>({
    queryKey: ["Boards"],
    queryFn: () => fetchBoardList(),
  });
};
