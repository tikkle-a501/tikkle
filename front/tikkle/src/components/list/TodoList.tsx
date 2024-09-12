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

  return (
    <Link href={`/chat/${chatId}`} passHref>
      <div className="inline-flex items-center p-6 gap-6 text-15 cursor-pointer">
        <Badge size="l" color="teal">
          {/* 추후 뱃지 컬러를 동적으로 받는 로직 작성 필요 */}
          {status}
        </Badge>
        <div>{formattedTime}까지</div>
        <div>{nickname}님과의 약속</div>
        <div className="text-warmGray500 truncate">{title}</div>
      </div>
    </Link>
  );
};

export default TodoList;
