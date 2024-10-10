import { fetchTodoAppointment } from "@/libs";

// 약속 생성 시 사용
export interface AppointmentRequest {
  roomId: string; // 채팅방 ID (UUID 형식)
  appTime: string; // 약속 시간
  timeQnt: number; // 약속에 사용할 시간(재화) 수량
}

// roomId로 약속 조회 시 사용
export interface BriefAppointment {
  appointmentId: string; // 약속 id
  appointmentTime: string; // 약속 시간
  timeQnt: number; // 약속에 사용할 시간(재화) 수량
}

// 내 할일 조회 응답
export interface TodoAppointmentResponse {
  appointmentId: string;
  status: string;
  partner: string;
  startTime: string;
  title: string;
  chatroomId: string;
}

export type TodoAppointmentResponses = TodoAppointmentResponse[];
