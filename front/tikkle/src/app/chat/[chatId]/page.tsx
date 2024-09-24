"use client";

import { useFetchChatroomById } from "@/hooks/chat/usefetchChatroomById";
import { useEffect, useState } from "react";

import { ChatroomData } from "@/types/chatroom/index.j";

import { usePathname } from "next/navigation";
import Image from "next/image";
import Button from "@/components/button/Button";
import Badge from "@/components/badge/Badge";
import Link from "next/link";
import ChatList from "@/components/chat/ChatList";

export default function ChatId() {
  const pathname = usePathname();

  // URL에서 roomId 추출 (예: '/chat/31000000-0000-0000-0000-000000000000')
  const roomId = pathname.split("/").pop(); // 경로의 마지막 부분이 roomId

  // 특정 유저 ID 설정
  const memberId = "74657374-3200-0000-0000-000000000000";
  const { data, error, isLoading } = useFetchChatroomById(roomId!);

  const [inputValue, setInputValue] = useState(""); // 입력 값 상태 관리

  if (isLoading) {
    return <p>Loading...</p>;
  }

  if (error) {
    return <p>Error: {error.message}</p>;
  }

  // 입력 값 변경 핸들러
  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setInputValue(event.target.value); // 입력 값 업데이트
  };

  return (
    <>
      {/* 채팅 헤더 */}
      <div className="item flex items-start justify-between self-stretch px-10 pb-0 pt-10">
        <div className="flex items-center gap-10">
          <Image
            src="/profile.png"
            alt={`${data?.partnerName} profile`}
            width={41}
            height={41}
            className="rounded-round"
          />
          <div className="flex py-10 text-28 font-bold text-teal-900">
            {data?.partnerName}님과의 대화
          </div>
        </div>
        <div>
          <Button size="m" variant="primary" design="fill" main="약속잡기" />
        </div>
      </div>
      <div className="flex items-center gap-6 self-stretch border-b border-b-coolGray300 p-10">
        {/* todo: 뱃지 색상을 status에 따라 동적으로 받는 로직 필요 */}
        <Badge size="l" color="yellow">
          {data?.status}
        </Badge>
        <Link href={`/board/${data?.boardId}`}>
          <div className="text-15">{data?.boardTitle}</div>
        </Link>
      </div>

      {/* 채팅 내용 */}
      <div className="scrollbar-custom flex flex-1 flex-col self-stretch overflow-y-auto">
        {data!.chats.length > 0 ? (
          data?.chats.map((chat, index) => (
            <ChatList
              key={index}
              content={chat.content}
              createdAt={chat.timestamp}
              senderId={chat.senderId}
              isMine={chat.senderId === memberId} // senderId와 memberId가 일치하면 true로 설정
            />
          ))
        ) : (
          <div className="flex h-full items-center justify-center">
            <p className="text-center text-warmGray500">
              아직 메시지가 없습니다.
            </p>
          </div>
        )}
      </div>

      {/* 채팅 인풋 */}
      <div className="h-42 flex items-center justify-center self-stretch rounded-10 border border-coolGray400 p-10">
        <input
          type="text"
          placeholder="내용을 입력하세요."
          value={inputValue}
          onChange={handleInputChange}
          className="flex-1 appearance-none bg-coolGray100 text-17 placeholder-warmGray300 focus:outline-none"
        />
      </div>
    </>
  );
}
