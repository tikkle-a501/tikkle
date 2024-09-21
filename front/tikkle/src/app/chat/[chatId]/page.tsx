"use client";

import { useState } from "react";
import { usePathname } from "next/navigation";
import Image from "next/image";
import Button from "@/components/button/Button";
import Badge from "@/components/badge/Badge";
import Link from "next/link";
import ChatList from "@/components/chat/ChatList";
import SearchInput from "@/components/input/SearchInput";

export default function ChatId() {
  const pathname = usePathname();

  const [inputValue, setInputValue] = useState(""); // 입력 값 상태 관리

  // 입력 값 변경 핸들러
  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setInputValue(event.target.value); // 입력 값 업데이트
  };

  const testChat = {
    profileImg: "/profile.png",
    nickname: "김싸피",
    status: "진행중",
    title: "테스트용 제목",
    boardId: "123",
  };

  const testChats = [
    {
      content: "Hello, this is a test message from user12!",
      createdAt: "2024-09-11T12:34:56Z",
      writerId: "user12",
      isMine: true,
    },
    {
      content:
        "Hi, this is another message from a different user! Hi, this is another message from a different user!",
      createdAt: "2024-09-11T13:45:30Z",
      writerId: "user34",
      isMine: false,
    },
    {
      content: "How are you doing today, user34?",
      createdAt: "2024-09-11T14:15:42Z",
      writerId: "user12",
      isMine: true,
    },
    {
      content: "I'm fine, thank you! How about you?",
      createdAt: "2024-09-11T15:00:10Z",
      writerId: "user34",
      isMine: false,
    },
    {
      content: "I'm doing great! Thanks for asking.",
      createdAt: "2024-09-11T15:30:22Z",
      writerId: "user12",
      isMine: true,
    },
    {
      content: "Are you available for the meeting tomorrow? Just checking in.",
      createdAt: "2024-09-11T16:05:18Z",
      writerId: "user34",
      isMine: false,
    },
    {
      content: "Yes, I'll be there on time. Looking forward to it!",
      createdAt: "2024-09-11T16:45:50Z",
      writerId: "user12",
      isMine: true,
    },
    {
      content: "Great! See you then.",
      createdAt: "2024-09-11T17:15:30Z",
      writerId: "user34",
      isMine: false,
    },
    {
      content: "By the way, did you review the document I sent?",
      createdAt: "2024-09-11T17:45:15Z",
      writerId: "user12",
      isMine: true,
    },
    {
      content: "Yes, I did. It looks good. I'll provide some feedback soon.",
      createdAt: "2024-09-11T18:10:00Z",
      writerId: "user34",
      isMine: false,
    },
  ];

  return (
    <>
      {/* 채팅 헤더 */}
      <div className="item flex items-start justify-between self-stretch px-10 pb-0 pt-10">
        <div className="flex items-center gap-10">
          <Image
            src={testChat.profileImg}
            alt={`${testChat.nickname} profile`}
            width={41}
            height={41}
            className="rounded-round"
          />
          <div className="flex py-10 text-28 font-bold text-teal-900">
            {testChat.nickname}님과의 대화
          </div>
        </div>
        <div>
          <Button size="m" variant="primary" design="fill" main="약속잡기" />
        </div>
      </div>
      <div className="flex items-center gap-6 self-stretch border-b border-b-coolGray300 p-10">
        {/* todo: 뱃지 색상을 status에 따라 동적으로 받는 로직 필요 */}
        <Badge size="l" color="yellow">
          {testChat.status}
        </Badge>
        <Link href={`/board/${testChat.boardId}`}>
          <div className="text-15">{testChat.title}</div>
        </Link>
      </div>

      {/* 채팅 내용 */}
      <div className="scrollbar-custom flex flex-1 flex-col self-stretch overflow-y-auto">
        {testChats.map((chat, index) => (
          <ChatList
            key={index}
            content={chat.content}
            createdAt={chat.createdAt}
            writerId={chat.writerId}
            isMine={chat.isMine}
          />
        ))}
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
