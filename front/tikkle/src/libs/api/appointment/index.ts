import api from "../client";
import { AppointmentRequest } from "@/types/appointment";

// 약속 생성 API
export const createAppointment = async (
  appointmentData: AppointmentRequest,
) => {
  try {
    const response = await api.post(`/appointment`, appointmentData);
    return response.data;
  } catch (error) {
    console.error("Error creating appointment:", error);
    throw new Error("Failed to create appointment");
  }
};

// roomId로 약속 조회
export const fetchAppointmentByRoomId = async (roomId: string) => {
  try {
    const response = await api.get(`/appointment/${roomId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching appointment by roomId:", error);
    throw new Error("Failed to fetch appointments");
  }
};

// appointmentId로 약속 삭제
export const deleteAppointmentById = async (appointmentId: string) => {
  try {
    const response = await api.delete(`/appointment/${appointmentId}`);
    return response.data;
  } catch (error) {
    console.error("Error deleting appointment:", error);
    throw new Error("Failed to delete appointment");
  }
};
