import { fetchBoardDetail } from "@/libs";
import { Board, BoardDetailResponse } from "@/types/board";
import { useQuery } from "@tanstack/react-query";

export const useFetchBoardDetail = (boardId: string) => {
  return useQuery<Board, Error>({
    queryKey: ["board", boardId],
    queryFn: () => fetchBoardDetail(boardId),
  });
};
