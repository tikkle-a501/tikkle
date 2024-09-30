import { useMutation } from "@tanstack/react-query";
import { createRate } from "@/libs/api/rate";

// 환율 생성 훅(테스트용으로만 쓰임)
export const useCreateRate = () => {
  return useMutation({
    mutationFn: createRate, // createRate 함수 호출
    onSuccess: (data) => {
      console.log("환율 생성 성공:", data); // 성공 시 처리 로직
    },
    onError: (error) => {
      console.error("환율 생성 중 오류 발생:", error); // 오류 처리
    },
  });
};
