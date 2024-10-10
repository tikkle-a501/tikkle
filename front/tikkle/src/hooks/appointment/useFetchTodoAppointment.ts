import { useQuery } from "@tanstack/react-query";
import { fetchTodoAppointment } from "@/libs/api/appointment";
import { TodoAppointmentResponses } from "@/types/appointment";

export const useFetchTodoAppointment = () => {
  return useQuery<TodoAppointmentResponses, Error>({
    queryKey: ["todo"],
    queryFn: () => fetchTodoAppointment(),
  });
};
