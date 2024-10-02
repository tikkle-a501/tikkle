import { useQuery } from "@tanstack/react-query";
import { fetchChatroomById } from "@/libs/api/chat";
import { ChatroomData } from "@/types/chatroom";

// 특정 채팅방 조회하는 React Query 훅
export const useFetchChatroomById = (roomId: string) => {
  return useQuery<ChatroomData, Error>({
    queryKey: ["chatroom", roomId], // roomId에 따라 캐시 관리
    queryFn: () => fetchChatroomById(roomId), // 데이터를 가져오는 함수
    enabled: !!roomId, // roomId가 있을 때만 호출
  });
};
