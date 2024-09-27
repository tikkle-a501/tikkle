import { deleteBoard, updateBoard } from "@/libs";
import { BoardRequest } from "@/types/board";
import { useMutation } from "@tanstack/react-query";

export const useDeleteBoard = () => {
  return useMutation<any, Error, string>({
    mutationFn: (boardId: string) => deleteBoard(boardId),
  });
};
