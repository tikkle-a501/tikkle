import { useQuery } from "@tanstack/react-query";
import { fetchChatroomsByUserId } from "@/libs/chat";
import { ChatroomResponses } from "@/types/chatroom/index.j";

// 특정 유저의 채팅 목록을 조회하는 훅
export const useFetchChatroomsByUserId = () => {
  return useQuery<ChatroomResponses, Error>({
    queryKey: ["chatrooms"], // 캐시 관리를 위한 queryKey
    queryFn: () => fetchChatroomsByUserId(), //    데이터를 가져오는 함수

    // 데이터가 없을 경우 빈 데이터를 기본값으로 반환
    initialData: {
      chatrooms: [], // 기본적으로 빈 배열을 설정
    },
  });
};
