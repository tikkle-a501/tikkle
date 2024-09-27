import { fetchBoardDetail } from "@/libs";
import { BoardDetailResponse } from "@/types/board";
import { useQuery } from "@tanstack/react-query";

export const useFetchBoardDetail = (boardId: string) => {
  return useQuery<BoardDetailResponse, Error>({
    queryKey: ["board", boardId],
    queryFn: () => fetchBoardDetail(boardId),
  });
};
