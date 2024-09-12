import MessageList from "@/components/chat/MessageList";

export default function ChatLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  // 더미 데이터
  const messages = [
    {
      chatId: "1",
      memberId: "user123",
      profileImage: "/profile.png",
      nickname: "John Doe",
      recentMessage: "Hello, how are you?",
      recentCreatedAt: "2024-09-11T12:34:56Z",
      isRead: false, // 읽지 않은 메시지
    },
    {
      chatId: "2",
      memberId: "user456",
      profileImage: "/profile.png",
      nickname: "Jane Smith",
      recentMessage: "Are you coming to the meeting? asdfasdfasdfasdfasdfasdf",
      recentCreatedAt: "2024-09-11T13:45:30Z",
      isRead: true, // 읽은 메시지
    },
  ];

  return (
    <>
      <div className="text-40 font-bold text-teal900">채팅 목록</div>
      <div className="flex gap-12 px-40 py-20">
        <div className="flex min-h-[480px] w-[344px] flex-col gap-12 rounded-12 border border-warmGray200 p-14">
          {messages.map((message, index) => (
            <MessageList
              key={index}
              chatId={message.chatId}
              memberId={message.memberId}
              profileImage={message.profileImage}
              nickname={message.nickname}
              recentMessage={message.recentMessage}
              recentCreatedAt={message.recentCreatedAt}
              isRead={message.isRead}
            />
          ))}
        </div>
        <div className="flex min-h-[480px] flex-grow items-center justify-center rounded-12 bg-coolGray100 p-5">
          {children}
        </div>
      </div>
    </>
  );
}
