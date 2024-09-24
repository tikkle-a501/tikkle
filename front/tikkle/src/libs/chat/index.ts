import {
  Chatroom,
  ChatroomResponse,
  ChatroomResponses,
} from "@/types/chatroom/index.j";
import { handleApiRequest } from "../client";

// 특정 유저의 채팅 목록 조회 API
export const fetchChatroomsByUserId = async (memberId: string) => {
  return handleApiRequest<ChatroomResponses, "get">(`/chatroom/`, "get");
};
