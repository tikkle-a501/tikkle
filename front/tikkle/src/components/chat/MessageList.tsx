"use client";

import { usePathname } from "next/navigation";
import Link from "next/link";

interface MessageItemProps {
  chatId: string; // select 판별용
  memberId: string; // 렌더링하지 않지만, props로 받음
  profileImage: string;
  nickname: string;
  recentMessage: string;
  recentCreatedAt: string; // 렌더링하지 않지만, props로 받음
  isRead: boolean;
}

const MessageList: React.FC<MessageItemProps> = ({
  chatId,
  memberId, // 사용하지 않지만, 필요할 경우를 위해 받음
  profileImage,
  nickname,
  recentMessage,
  recentCreatedAt, // 사용하지 않지만, 필요할 경우를 위해 받음
  isRead,
}) => {
  const pathname = usePathname();
  const isSelected = pathname === `/chat/${chatId}`; // 현재 경로와 chatId가 일치하는지 확인

  return (
    <Link href={`/chat/${chatId}`} passHref>
      <div
        className={`flex flex-grow items-center p-10 gap-10 rounded-10 cursor-pointer ${
          isSelected ? "bg-[rgba(20,184,166,0.31)]" : ""
        }`}
      >
        {/* 상대방 프로필 이미지 */}
        <img
          src={profileImage}
          alt={`${nickname}'s profile`}
          className="w-40 h-40 rounded-round"
        />

        {/* 메시지 내용 */}
        <div className="flex flex-col flex-grow min-w-0 gap-4">
          <div className="flex items-center gap-10">
            {/* 상대방 닉네임 */}
            <span className="text-15 font-semibold text-black">{nickname}</span>
            {/* 읽음 여부 표시 */}
            {!isRead && (
              <div className="w-10 h-10 rounded-round bg-rose500"></div>
            )}
          </div>

          {/* 최근 메시지 */}
          <div className="text-14 truncate">{recentMessage}</div>
        </div>
      </div>
    </Link>
  );
};

export default MessageList;
