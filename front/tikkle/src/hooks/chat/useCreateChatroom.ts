import { useMutation } from "@tanstack/react-query";
import { createChatroom } from "@/libs";

export const useCreateChatroom = () => {
  return useMutation<any, Error, string>({
    mutationFn: (boardId: string) => createChatroom(boardId),
  });
};
