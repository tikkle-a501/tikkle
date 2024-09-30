import { deleteBoard, updateBoard } from "@/libs";
import { BoardRequest } from "@/types/board";
import { useMutation } from "@tanstack/react-query";

export const useDeleteBoard = (boardId: string) => {
  return useMutation<any, Error, string>({
    mutationFn: () => deleteBoard(boardId),
  });
};
