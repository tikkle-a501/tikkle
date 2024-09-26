import api from "../client";
import { Appointment } from "@/types/appointment";

// 약속 생성 API
export const createAppointment = async (appointmentData: Appointment) => {
  try {
    const response = await api.post(`/appointment`, appointmentData);
    return response.data;
  } catch (error) {
    console.error("Error creating appointment:", error);
    throw new Error("Failed to create appointment");
  }
};
