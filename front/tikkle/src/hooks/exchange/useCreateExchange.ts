import { useMutation } from "@tanstack/react-query";
import { handleSuccess, handleError } from "@/libs/utils";
import { ExchangeCreateRequest } from "@/types";
import { createExchange } from "@/libs";

export const useCreateExchange = () => {
  return useMutation<void, Error, ExchangeCreateRequest>({
    mutationFn: (data: ExchangeCreateRequest) => createExchange(data),
    onSuccess: (data) => {
      handleSuccess("✅ Exchange created successfully", data);
    },
    onError: (error: Error) => {
      handleError("😥 Failed to create exchange", error);
    },
  });
};
