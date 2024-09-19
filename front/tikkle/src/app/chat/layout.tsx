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
      isRead: false,
    },
    {
      chatId: "2",
      memberId: "user456",
      profileImage: "/profile.png",
      nickname: "Jane Smith",
      recentMessage: "Are you coming to the meeting? asdfasdfasdfasdfasdfasdf",
      recentCreatedAt: "2024-09-11T13:45:30Z",
      isRead: true,
    },
    {
      chatId: "3",
      memberId: "user789",
      profileImage: "/profile.png",
      nickname: "Alice Johnson",
      recentMessage: "Don't forget to submit your report.",
      recentCreatedAt: "2024-09-11T14:10:20Z",
      isRead: false,
    },
    {
      chatId: "4",
      memberId: "user101",
      profileImage: "/profile.png",
      nickname: "Bob Brown",
      recentMessage: "Let's catch up later.",
      recentCreatedAt: "2024-09-11T15:22:45Z",
      isRead: true,
    },
    {
      chatId: "5",
      memberId: "user202",
      profileImage: "/profile.png",
      nickname: "Charlie Green",
      recentMessage: "Meeting postponed to tomorrow.",
      recentCreatedAt: "2024-09-11T16:30:12Z",
      isRead: false,
    },
    {
      chatId: "6",
      memberId: "user303",
      profileImage: "/profile.png",
      nickname: "David Blue",
      recentMessage: "Could you review the document?",
      recentCreatedAt: "2024-09-11T17:15:33Z",
      isRead: true,
    },
    {
      chatId: "7",
      memberId: "user404",
      profileImage: "/profile.png",
      nickname: "Eve White",
      recentMessage: "Happy to help!",
      recentCreatedAt: "2024-09-11T18:02:21Z",
      isRead: false,
    },
    {
      chatId: "8",
      memberId: "user505",
      profileImage: "/profile.png",
      nickname: "Frank Black",
      recentMessage: "Can we reschedule our call?",
      recentCreatedAt: "2024-09-11T19:48:10Z",
      isRead: true,
    },
    {
      chatId: "9",
      memberId: "user606",
      profileImage: "/profile.png",
      nickname: "Grace Red",
      recentMessage: "The presentation was great!",
      recentCreatedAt: "2024-09-11T20:30:40Z",
      isRead: false,
    },
    {
      chatId: "10",
      memberId: "user707",
      profileImage: "/profile.png",
      nickname: "Hank Yellow",
      recentMessage: "I'll send the files shortly.",
      recentCreatedAt: "2024-09-11T21:50:05Z",
      isRead: true,
    },
  ];

  return (
    <>
      <div className="text-40 font-bold text-teal900">채팅 목록</div>
      <div className="flex gap-12 px-40 py-20">
        <div className="scrollbar-custom flex h-[767px] w-[344px] flex-col gap-12 overflow-y-auto rounded-12 border border-warmGray200 p-14">
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
        <div className="flex h-[767px] flex-grow flex-col items-center justify-center rounded-12 bg-coolGray100 p-20">
          {children}
        </div>
      </div>
    </>
  );
}
