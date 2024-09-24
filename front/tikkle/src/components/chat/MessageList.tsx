"use client";

import { usePathname } from "next/navigation";
import Link from "next/link";

interface MessageItemProps {
  roomId: string; // select 판별용
  profileImage: string;
  partner: string;
  lastMsg: string;
  recentCreatedAt: string; // 렌더링하지 않지만, props로 받음
  isRead: boolean;
}

const MessageList: React.FC<MessageItemProps> = ({
  roomId,
  profileImage,
  partner,
  lastMsg,
  recentCreatedAt, // 사용하지 않지만, 필요할 경우를 위해 받음
  isRead,
}) => {
  const pathname = usePathname();
  const isSelected = pathname === `/chat/${roomId}`; // 현재 경로와 chatId가 일치하는지 확인

  return (
    <Link href={`/chat/${roomId}`} passHref>
      <div
        className={`flex flex-grow cursor-pointer items-center gap-10 rounded-10 p-10 ${
          isSelected ? "bg-[rgba(20,184,166,0.31)]" : ""
        }`}
      >
        {/* 상대방 프로필 이미지 */}
        <img
          src={profileImage}
          alt={`${partner}'s profile`}
          className="h-40 w-40 rounded-round"
        />

        {/* 메시지 내용 */}
        <div className="flex min-w-0 flex-grow flex-col gap-4">
          <div className="flex items-center gap-10">
            {/* 상대방 닉네임 */}
            <span className="text-15 font-semibold text-black">{partner}</span>
            {/* 읽음 여부 표시 */}
            {!isRead && (
              <div className="h-10 w-10 rounded-round bg-rose500"></div>
            )}
          </div>

          {/* 최근 메시지 */}
          <div className="truncate text-14">{lastMsg}</div>
        </div>
      </div>
    </Link>
  );
};

export default MessageList;
