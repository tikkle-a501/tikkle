"use client";

import Link from "next/link";
import Badge from "../badge/Badge";

interface TodoListProps {
  status: string; // 상태
  appointmentTime: string; // 약속 시간
  nickname: string; // 닉네임
  title: string; // 제목
  chatId: string; // 채팅 ID
}

const TodoList: React.FC<TodoListProps> = ({
  status,
  appointmentTime,
  nickname,
  title,
  chatId,
}) => {
  // 오늘 날짜의 약속만 보인다고 가정
  const formattedTime = new Date(appointmentTime).toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
    hour12: true, // 12시간제로 오전/오후 표시
  });

  const getBadgeColor = (status: string) => {
    switch (status) {
      case "진행전":
        return "red";
      case "진행중":
        return "yellow";
      case "완료":
        return "gray";
      default:
        return "red";
    }
  };

  return (
    <Link href={`/chat/${chatId}`} passHref>
      <div className="inline-flex cursor-pointer items-center gap-6 p-6 text-15">
        <Badge size="l" color={getBadgeColor(status)}>
          {status}
        </Badge>
        <div>{formattedTime}까지</div>
        <div>{nickname}님과의 약속</div>
        <div className="truncate font-400 text-warmGray500">{title}</div>
      </div>
    </Link>
  );
};

export default TodoList;
