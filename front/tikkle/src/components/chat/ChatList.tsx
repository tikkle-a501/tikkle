interface ChatProps {
  content: string;
  timestamp: number[]; // timestamp 배열을 받음
  senderId: string;
  isMine: boolean;
}

const ChatList: React.FC<ChatProps> = ({
  content,
  timestamp,
  senderId,
  isMine,
}) => {
  const sideStyle = isMine
    ? "items-end" // Variant 1: 현재 사용자의 스타일
    : "items-start"; // Variant 2: 다른 사용자의 스타일

  const bubbleStyle = isMine
    ? "bg-teal400 text-black rounded-[12px_0_12px_12px]" // Variant 1: 현재 사용자의 스타일
    : "bg-coolGray200 text-black rounded-[0_12px_12px_12px]"; // Variant 2: 다른 사용자의 스타일

  const dateStyle = isMine
    ? "justify-end" // Variant 1: 현재 사용자의 스타일
    : "flex-start"; // Variant 2: 다른 사용자의 스타일

  // timestamp 배열을 Date 객체로 변환
  const messageDate = new Date(
    timestamp[0], // year
    timestamp[1] - 1, // month (0-based)
    timestamp[2], // day
    timestamp[3], // hour
    timestamp[4], // minute
    timestamp[5], // second
    Math.floor(timestamp[6] / 1000000), // nanoseconds -> milliseconds로 변환
  );

  return (
    <div className={`flex flex-col ${sideStyle} gap-4 px-14 py-[17px]`}>
      <div
        className={`flex p-12 text-15 text-base ${bubbleStyle} max-w-[400px]`}
      >
        {content}
      </div>
      <div className={`${dateStyle} pr-10 text-13 text-coolGray500`}>
        {messageDate.toLocaleString()}
      </div>
    </div>
  );
};

export default ChatList;
