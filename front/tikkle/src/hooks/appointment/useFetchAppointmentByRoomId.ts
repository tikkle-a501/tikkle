import { useQuery } from "@tanstack/react-query";
import { fetchAppointmentByRoomId } from "@/libs/api/appointment";
import { BriefAppointment } from "@/types/appointment";

// roomId로 약속 조회하는 useQuery 훅
export const useFetchAppointmentByRoomId = (roomId: string) => {
  return useQuery<BriefAppointment, Error>({
    queryKey: ["appointment", roomId], // 캐시 관리를 위한 queryKey
    queryFn: () => fetchAppointmentByRoomId(roomId), // 데이터를 가져오는 함수
  });
};
