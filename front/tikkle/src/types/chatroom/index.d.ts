import { Chat } from "../chat";

export interface Chatroom {
  roomId: string;
  partner: string;
  lastSender: string;
  lastMsg: string;
}

export interface ChatroomData {
  boardId: string;
  boardTitle: string;
  chats: Chat[];
  partnerName: string;
  status: string;
  deletedAt: boolean;
}

export interface ChatroomResponse {
  chatroom: ChatroomData;
}

export type ChatroomResponses = Chatroom[];
