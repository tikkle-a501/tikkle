import { AppointmentRequest, BriefAppointment } from "@/types/appointment";
import { handleApiRequest } from "../client";

// 약속 생성 API
export const createAppointment = async (
  appointmentData: AppointmentRequest,
) => {
  return handleApiRequest<void, "post", AppointmentRequest>(
    `/appointment`,
    "post",
    appointmentData,
  );
};

// roomId로 약속 조회
export const fetchAppointmentByRoomId = async (roomId: string) => {
  return handleApiRequest<BriefAppointment, "get">(
    `/appointment/${roomId}`,
    "get",
  );
};

// appointmentId로 약속 삭제
export const deleteAppointmentById = async (appointmentId: string) => {
  return handleApiRequest<void, "delete">(
    `/appointment/${appointmentId}`,
    "delete",
  );
};
