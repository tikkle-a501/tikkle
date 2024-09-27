import { updateBoard } from "@/libs";
import { BoardRequest } from "@/types/board";
import { useMutation } from "@tanstack/react-query";

export const useUpdateBoard = () => {
  return useMutation<any, Error, { boardId: string; boardData: BoardRequest }>({
    mutationFn: ({ boardId, boardData }) => updateBoard(boardId, boardData),
  });
};
