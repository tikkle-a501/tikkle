import { useMutation } from "@tanstack/react-query";
import { handleSuccess, handleError } from "@/libs/utils";
import { ExchangeCreateRequest } from "@/types";
import { createExchange } from "@/libs";

export const useCreateExchange = (data: ExchangeCreateRequest) => {
  return useMutation<void, Error, { data: ExchangeCreateRequest }>({
    mutationFn: () => createExchange(data),
    onSuccess: (data) => {
      handleSuccess("✅ Exchange created successfully", data);
    },
    onError: (error: Error) => {
      handleError("😥 Failed to create exchange", error);
    },
  });
};
