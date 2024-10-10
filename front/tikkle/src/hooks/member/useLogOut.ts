import { useMutation } from "@tanstack/react-query";
import { logOut } from "@/libs";
import { useRouter } from "next/navigation";
import { useMypageStore } from "@/store/mypageStore"; // zustand 스토어 가져오기

export const useLogOut = () => {
  const router = useRouter();
  const clearMember = useMypageStore((state) => state.clearMember); // zustand에서 clearMember 가져오기
  return useMutation({
    mutationFn: () => logOut(),
    onSuccess: () => {
      // 로그아웃 성공 후 처리
      clearMember();
      // 예를 들어, 페이지 이동
      router.push("/");
    },
    onError: (error: Error) => {
      // 에러 발생 시 처리
      console.error("Error during logout:", error.message);
    },
  });
};
