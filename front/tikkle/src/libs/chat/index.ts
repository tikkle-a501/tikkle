import api from "../client";

// 특정 유저의 채팅 목록 조회 API
export const fetchChatroomsByUserId = async () => {
  try {
    const response = await api.get(`/chatroom`);
    return response.data;
  } catch (error) {
    console.error("Error fetching chatrooms:", error);
    throw new Error("Failed to fetch chatrooms");
  }
};