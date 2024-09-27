import { createBoard } from "@/libs";
import { BoardRequest } from "@/types/board";
import { useMutation } from "@tanstack/react-query";

export const useCreateBoard = () => {
  return useMutation<any, Error, BoardRequest>({
    mutationFn: (boardData: BoardRequest) => createBoard(boardData),
  });
};
