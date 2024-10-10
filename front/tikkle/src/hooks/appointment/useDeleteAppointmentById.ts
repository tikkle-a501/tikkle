import { useMutation, useQueryClient } from "@tanstack/react-query";
import { deleteAppointmentById } from "@/libs";

export const useDeleteAppointmentById = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (appointmentId: string) => deleteAppointmentById(appointmentId),
    onSuccess: () => {
      // 성공 시 약속 관련 데이터를 최신 상태로 업데이트
      queryClient.invalidateQueries({ queryKey: ["appointments"] });
      alert("약속이 성공적으로 취소되었습니다.");
    },
    onError: (error: any) => {
      console.error("Error deleting appointment:", error);
      throw new Error("Failed to delete appointment");
    },
  });
};
