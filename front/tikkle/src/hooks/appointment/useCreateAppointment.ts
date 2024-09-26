import { useMutation } from "@tanstack/react-query";
import { createAppointment } from "@/libs/api/appointment";
import { Appointment } from "@/types/appointment";

// 약속 생성 훅
export const useCreateAppointment = () => {
  return useMutation<any, Error, Appointment>({
    mutationFn: (appointmentData: Appointment) =>
      createAppointment(appointmentData),
  });
};
