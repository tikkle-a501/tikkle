"use client";

import { useState, useEffect } from "react";
import Button from "../button/Button";
import PromiseInput from "../input/PromiseInput";
import { useCreateAppointment } from "@/hooks/appointment/useCreateAppointment";
import Loading from "../loading/Loading";

interface PromiseDropdownProps {
  roomId: string;
  refetchAppointment: () => void;
  refetchChatroom: () => void;
}

const PromiseDropdown: React.FC<PromiseDropdownProps> = ({
  roomId,
  refetchAppointment,
  refetchChatroom,
}) => {
  // 상태로 form 입력값 관리
  const [timeQnt, setTimeQnt] = useState<number>(0);
  const [month, setMonth] = useState<number>(0);
  const [day, setDay] = useState<number>(0);
  const [hour, setHour] = useState<number>(0);
  const [minute, setMinute] = useState<number>(0);

  // useCreateAppointment 훅을 호출
  const { mutate, isPending, isError } = useCreateAppointment();

  useEffect(() => {
    const now = new Date();
    setMonth(now.getMonth() + 1);
    setDay(now.getDate());
    setHour(now.getHours());
    setMinute(now.getMinutes());
  }, []);

  const handleSubmit = () => {
    const appTime = new Date(2024, month - 1, day, hour, minute).toISOString();

    const appointmentData = {
      roomId,
      appTime,
      timeQnt,
    };

    // mutate 함수를 호출하여 약속 생성
    mutate(appointmentData, {
      onSuccess: () => {
        console.log("Appointment created successfully");
        refetchAppointment(); // 약속 생성 후 refetchAppointment 호출
        // refetchChatroom();
      },
      onError: (error) => {
        console.error("Error creating appointment:", error);
      },
    });
  };

  const isSubmitDisabled =
    timeQnt === 0 || month === 0 || day === 0 || hour === 0 || minute === 0;

  return (
    <div className="absolute right-0 flex h-[260px] w-[230px] flex-col justify-center gap-10 rounded-12 bg-white p-20 text-15 text-teal900 shadow-s">
      {/* 타이틀 */}
      <div className="flex text-24 font-bold text-teal900">약속 잡기</div>

      {/* 시간(재화) 입력 */}
      <div className="flex min-w-0 items-center justify-end gap-10 self-stretch px-0 py-10">
        <PromiseInput
          autoFocus
          value={timeQnt || ""}
          onChange={(e) => setTimeQnt(Number(e.target.value))}
        />
        <div className="whitespace-nowrap">시간</div>
      </div>

      {/* 날짜 입력 */}
      <div className="flex appearance-none items-center justify-end gap-8 self-stretch border-none px-0 py-10 outline-none">
        <PromiseInput
          value={month || ""}
          onChange={(e) => setMonth(Number(e.target.value))}
        />
        <div>월</div>
        <PromiseInput
          value={day || ""}
          onChange={(e) => setDay(Number(e.target.value))}
        />
        <div>일</div>
        <PromiseInput
          value={hour || ""}
          onChange={(e) => setHour(Number(e.target.value))}
        />
        <div>시</div>
        <PromiseInput
          value={minute || ""}
          onChange={(e) => setMinute(Number(e.target.value))}
        />
        <div>분</div>
      </div>

      {/* 에러 메시지 */}
      {isError && (
        <p className="text-red-500">
          Error occurred while creating appointment.
        </p>
      )}

      {/* 로딩 중일 때 */}
      {isPending ? (
        <Loading />
      ) : (
        <Button
          size="s"
          variant="primary"
          design="fill"
          main="확인"
          onClick={handleSubmit}
          disabled={isSubmitDisabled}
        />
      )}
    </div>
  );
};

export default PromiseDropdown;
