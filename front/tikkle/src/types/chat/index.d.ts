export interface Chat {
  senderId: string;
  content: string;
  timestamp: Date; // Java의 LocalDateTime을 Date로 매핑
}
