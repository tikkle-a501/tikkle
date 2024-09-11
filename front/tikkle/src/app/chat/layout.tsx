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
      <div className="text-40 text-teal900 font-bold">채팅 목록</div>
      <div className="flex px-40 py-20 gap-12">
        <div className="flex flex-col w-[344px] min-h-[480px] p-14 gap-12 rounded-12 border border-warmGray200">
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
        <div className="flex flex-grow min-h-[480px] items-center justify-center p-5 rounded-12 bg-coolGray100">
          {children}
        </div>
      </div>
    </>
  );
}
