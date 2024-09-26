export interface Appointment {
  roomId: string; // 채팅방 ID (UUID 형식)
  appTime: string; // 약속 시간
  timeQnt: number; // 약속에 사용할 시간(재화) 수량
}
