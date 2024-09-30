import { handleApiRequest } from "../client";
import { ChatroomResponses, ChatroomData } from "@/types";

// 특정 유저의 채팅 목록 조회 API
export const fetchChatroomsByUserId = async () => {
  return handleApiRequest<ChatroomResponses, "get">(`/chatroom`, "get");
};

// 특정 채팅방 조회 API (roomId를 파라미터로 받음)
export const fetchChatroomById = async (roomId: string) => {
  return handleApiRequest<ChatroomData, "get">(`/chatroom/${roomId}`, "get");
};
