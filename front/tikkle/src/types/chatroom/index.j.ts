export interface Chatroom {
  id: string;
  createdAt: string;
  boardId: string;
  performerId: string;
  writerId: string;

  profileImage: string;
  nickname: string;
  recentMessage: string;
  isRead: boolean;
}

export interface ChatroomResponse {
  chatroom: Chatroom;
}

export interface ChatroomResponses {
  chatrooms: Chatroom[];
}

export interface Response<T> {
  success: boolean;
  data: T;
  message: string;
}
