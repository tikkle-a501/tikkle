"use client";

import MessageList from "@/components/chat/MessageList";
import Loading from "@/components/loading/Loading";
import { useFetchChatroomsByUserId } from "@/hooks/chat/useFetchChatroomsByUserId";

export default function ChatLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  // 특정 유저 ID 설정
  const memberId = "74657374-3200-0000-0000-000000000000";

  // useFetchChatroomsByUserId 훅을 사용하여 채팅 목록을 가져옴
  const { data, error, isLoading } = useFetchChatroomsByUserId(memberId);

  // 로딩 중일 때 표시할 내용
  if (isLoading) {
    return (
      <>
        <div className="text-40 font-bold text-teal900">채팅 목록</div>
        <Loading />
      </>
    );
  }

  // 에러 발생 시 표시할 내용
  if (error) {
    return <p>Error fetching chatrooms: {error.message}</p>;
  }

  // 데이터 확인용 콘솔 로그
  console.log("Fetched chatroom data:", data);

  return (
    <>
      <div className="text-40 font-bold text-teal900">채팅 목록</div>
      <div className="flex gap-12 px-40 py-20">
        <div className="scrollbar-custom flex h-[767px] w-[344px] flex-col gap-12 overflow-y-auto rounded-12 border border-warmGray200 p-14">
          {data?.chatrooms.map((message, index) => (
            <MessageList
              key={index}
              chatId={message.id}
              memberId={message.performerId}
              profileImage={message.profileImage}
              nickname={message.nickname}
              recentMessage={message.recentMessage}
              recentCreatedAt={message.createdAt}
              isRead={message.isRead}
            />
          ))}
        </div>
        <div className="flex h-[767px] flex-grow flex-col items-center justify-center rounded-12 bg-coolGray100 p-20">
          {children}
        </div>
      </div>
    </>
  );
}
