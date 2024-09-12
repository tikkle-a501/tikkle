"use client";

import { useState, useEffect } from "react";

interface ChatProps {
  content: string;
  createdAt: string;
  writerId: string;
}

const ChatList: React.FC<ChatProps> = ({ content, createdAt, writerId }) => {
  // 추후 store로부터 userId를 가져오는 로직이 추가될 예정
  const userId = "user12"; // 현재 하드코딩된 userId (스토어 작성 후 교체 예정)
  const [isCurrentUser, setIsCurrentUser] = useState<boolean>(false);
  // writerId와 userId가 일치하는지 확인하여 스타일을 조건부로 설정

  // userId가 변경될 때마다 isCurrentUser 값 설정
  useEffect(() => {
    if (userId) {
      setIsCurrentUser(writerId === userId);
    }
  }, [userId, writerId]); // userId 또는 writerId가 변경될 때마다 실행

  const sideStyle = isCurrentUser
    ? "items-end" // Variant 1: 현재 사용자의 스타일
    : "items-start"; // Variant 2: 다른 사용자의 스타일

  const bubbleStyle = isCurrentUser
    ? "bg-teal400 text-black rounded-[12px_0_12px_12px]" // Variant 1: 현재 사용자의 스타일
    : "bg-coolGray200 text-black rounded-[0_12px_12px_12px]"; // Variant 2: 다른 사용자의 스타일

  const dateStyle = isCurrentUser
    ? "justify-end" // Variant 1: 현재 사용자의 스타일
    : "flex-start"; // Variant 2: 다른 사용자의 스타일

  return (
    <div className={`flex flex-col ${sideStyle} px-14 py-[17px] gap-4`}>
      <div
        className={`flex p-12 text-base text-15 ${bubbleStyle} max-w-[400px]`}
      >
        {content}
      </div>
      <div className={`${dateStyle} pr-10 text-13 text-coolGray500`}>
        {new Date(createdAt).toLocaleString()}
      </div>
    </div>
  );
};

export default ChatList;
