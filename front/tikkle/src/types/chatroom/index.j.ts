export interface Chatroom {
  roomId: string;
  partner: string;
  lastSender: string;
  lastMsg: string;
}

export interface ChatroomResponse {
  chatroom: Chatroom;
}

export type ChatroomResponses = Chatroom[];

export interface Response<T> {
  success: boolean;
  data: T;
  message: string;
}
